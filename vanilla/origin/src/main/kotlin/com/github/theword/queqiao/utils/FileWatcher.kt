package com.github.theword.queqiao.utils

import org.slf4j.Logger
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.BasicFileAttributes

/**
 * 日志 tail：单线程 200ms 轮询，避免 WatchService 事件丢失和额外的刷新线程。
 *
 * <p>只处理完整行；文件轮转或截断时重置指针；读取异常保留原指针，下一轮重试。
 */
class FileWatcher(
    private val logPath: Path,
    private val charset: Charset,
    private val lineProcessor: LineProcessor,
    private val logger: Logger,
    private val pollIntervalMs: Long = DEFAULT_POLL_INTERVAL_MS
) {

    @Volatile
    private var running = true

    private var position = 0L
    private var fileKey: Any? = null
    private var initialized = false
    private val pending = ByteArrayOutputStream()

    fun run() {
        while (running) {
            try {
                pollOnce()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                return
            } catch (e: Exception) {
                logger.warn("监听日志时出现异常：{}", e.message, e)
            }

            try {
                Thread.sleep(pollIntervalMs)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                return
            }
        }
    }

    fun stop() {
        running = false
    }

    private fun pollOnce() {
        if (!Files.isRegularFile(logPath)) {
            if (initialized) {
                // 文件被删除/移走：清空指针，等新文件出现后从头读取
                reset()
                fileKey = null
            }
            return
        }

        val attributes = Files.readAttributes(logPath, BasicFileAttributes::class.java)
        val currentKey = attributes.fileKey()

        if (!initialized) {
            // 启动时从文件末尾开始，避免重放历史日志
            initialized = true
            fileKey = currentKey
            position = attributes.size()
            return
        }

        if (currentKey != null && currentKey != fileKey) {
            logger.info("检测到日志文件轮转，从头读取：{}", logPath)
            reset()
            fileKey = currentKey
        } else if (attributes.size() < position) {
            logger.info("检测到日志文件截断，从头读取：{}", logPath)
            reset()
        }

        if (attributes.size() > position) {
            readAvailable()
        }
    }

    private fun reset() {
        position = 0L
        pending.reset()
    }

    private fun readAvailable() {
        Files.newByteChannel(logPath, StandardOpenOption.READ).use { channel ->
            channel.position(position)
            val buffer = ByteBuffer.allocate(READ_BUFFER_BYTES)
            while (running) {
                buffer.clear()
                val read = channel.read(buffer)
                if (read <= 0) {
                    break
                }
                buffer.flip()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                position += read
                pending.write(bytes)
                drainCompleteLines()
            }
        }
    }

    private fun drainCompleteLines() {
        val data = pending.toByteArray()
        var lastNewline = -1
        for (index in data.indices.reversed()) {
            if (data[index] == '\n'.code.toByte()) {
                lastNewline = index
                break
            }
        }

        if (lastNewline < 0) {
            // 没有完整行：保留半行；异常超长时放弃，避免内存无限增长
            if (data.size > MAX_PENDING_BYTES) {
                logger.warn("日志中存在超过 {} 字节且没有换行的内容，已丢弃", MAX_PENDING_BYTES)
                pending.reset()
            }
            return
        }

        val completeBytes = data.copyOfRange(0, lastNewline)
        val remainingBytes = data.copyOfRange(lastNewline + 1, data.size)
        pending.reset()
        pending.write(remainingBytes)

        val text = String(completeBytes, charset)
        for (line in text.split('\n')) {
            val cleaned = line.removeSuffix("\r")
            if (cleaned.isNotEmpty() && cleaned.length < MAX_LINE_LENGTH) {
                try {
                    lineProcessor.processLine(cleaned)
                } catch (e: Exception) {
                    logger.warn("处理日志行失败：{}", cleaned, e)
                }
            }
        }
    }

    companion object {

        private const val DEFAULT_POLL_INTERVAL_MS = 200L
        private const val READ_BUFFER_BYTES = 8192
        private const val MAX_PENDING_BYTES = 1024 * 1024
        private const val MAX_LINE_LENGTH = 320
    }
}
