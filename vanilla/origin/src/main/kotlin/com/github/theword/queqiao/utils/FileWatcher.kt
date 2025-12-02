package com.github.theword.queqiao.utils

import com.github.theword.queqiao.tool.GlobalContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.util.concurrent.atomic.AtomicLong


class FileWatcher(private val lineProcessor: LineProcessor) {

    /**
     * 文件监控
     * 同步调用会阻塞
     *
     * @param filePath String
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    @Throws(IOException::class, InterruptedException::class)
    fun watcherLog(filePath: Path) {
        val fileName = filePath.fileName.toString()
        val parentPath = filePath.parent
        val watchService = FileSystems.getDefault().newWatchService()

        parentPath.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_DELETE
        )
        val lastPointer = AtomicLong(0L)
        do {
            val key = watchService.take()
            val watchEvents = key.pollEvents()
            watchEvents.stream()
                .filter { i: WatchEvent<*> -> StandardWatchEventKinds.ENTRY_MODIFY === i.kind() && fileName == (i.context() as Path).fileName.toString() }
                .forEach { i: WatchEvent<*> ->
                    if (i.count() > 1) {
                        return@forEach
                    }
                    val configFile = Paths.get(parentPath.toString() + "/" + i.context()).toFile()
                    val str = StringBuilder()
                    lastPointer.set(getFileContent(configFile, lastPointer.get(), str))

                    val line = str.toString()
                    if (line.isNotEmpty() && line.length < 320 && GlobalContext.getConfig().isEnable) {
                        lineProcessor.processLine(line)
                    }
                }
            key.reset()
        } while (true)
    }

    /**
     * beginPointer > configFile 时会从头读取
     *
     * @param configFile   配置文件
     * @param beginPointer 起点
     * @param str          内容会拼接进去
     * @return 读到了多少字节, -1 读取失败
     */
    private fun getFileContent(configFile: File, beginPointer: Long, str: StringBuilder): Long {
        var currentPointer = beginPointer
        if (currentPointer < 0) {
            currentPointer = 0
        }
        var file: RandomAccessFile? = null
        var top = true
        try {
            file = RandomAccessFile(configFile, "r")
            if (currentPointer > file.length()) {
                return 0
            }
            file.seek(currentPointer)
            var line: String
            while ((file.readLine().also { line = it }) != null) {
                if (top) {
                    top = false
                } else {
                    str.append("\n")
                }
                str.append(String(line.toByteArray(StandardCharsets.ISO_8859_1), charset("GBK")))
            }
            return file.filePointer
        } catch (e: IOException) {
            GlobalContext.getLogger().error("Error reading file content", e)
            return -1
        } finally {
            if (file != null) {
                try {
                    file.close()
                } catch (e: IOException) {
                    GlobalContext.getLogger().error("Error closing file", e)
                }
            }
        }
    }

    companion object {
        /**
         * 文件监听
         *
         * @param filePath 文件路径
         */
        fun fileListen(filePath: String, lineProcessor: LineProcessor) {
            val path = Paths.get(filePath)
            // 监听
            Thread {
                try {
                    FileWatcher(lineProcessor).watcherLog(path)
                } catch (e: Exception) {
                    GlobalContext.getLogger().error("监听日志时出现异常", e)
                }
            }.start()

            // 刷新日志
            Thread {
                try {
                    while (true) {
                        val writer = FileWriter(path.toFile(), true)
                        writer.flush()
                        writer.close()
                    }
                } catch (e: IOException) {
                    GlobalContext.getLogger().error("写日志时出现异常", e)
                }
            }.start()
        }
    }
}