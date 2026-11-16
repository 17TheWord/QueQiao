package com.github.theword.queqiao.utils

import com.google.gson.JsonElement
import io.github.theword.queqiao.core.exception.rcon.RconException
import io.github.theword.queqiao.core.platform.PlatformResult
import io.github.theword.queqiao.core.platform.PlatformResultCode
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import org.slf4j.Logger
import java.nio.charset.StandardCharsets

/**
 * 通过 RCON 向服务端下发消息。
 *
 * <p>独立端没有服务端 API，只能走 RCON。RCON 命令是纯文本，
 * 且 Minecraft 单个 RCON 命令上限为 1446 字节（UTF-8），因此这里负责：
 * 组件转纯文本、清理控制字符、按字节安全截断、识别原版命令错误。
 */
class RconMessageSender(
    private val runtime: QueQiaoRuntime?,
    private val prefixProvider: () -> String?,
    private val logger: Logger
) {

    fun broadcast(component: String?): PlatformResult<Void> {
        val text = buildText(component, applyPrefix = true)
        if (text.isEmpty()) {
            return PlatformResult.failure(PlatformResultCode.INVALID_ARGUMENT, "消息内容为空")
        }
        return sendWithLimit("say ", text)
    }

    fun sendTitle(
        title: String?,
        subtitle: String?,
        fadeIn: Int,
        stay: Int,
        fadeOut: Int
    ): PlatformResult<Void> {
        val failures = mutableListOf<String>()

        sendCommand("title @a times $fadeIn $stay $fadeOut")?.let { failures.add(it) }

        sendComponentCommand("title @a title ", title)?.let { failures.add(it) }
        sendComponentCommand("title @a subtitle ", subtitle)?.let { failures.add(it) }

        if (failures.isNotEmpty()) {
            return PlatformResult.failure(PlatformResultCode.FAILED, failures.joinToString("；"))
        }
        return PlatformResult.success(null)
    }

    fun sendActionBar(component: String?): PlatformResult<Void> {
        val text = buildText(component, applyPrefix = false)
        if (text.isEmpty()) {
            return PlatformResult.failure(PlatformResultCode.INVALID_ARGUMENT, "ActionBar 内容为空")
        }
        return sendWithLimit("title @a actionbar ", text)
    }

    /**
     * 发送一个带文本内容的 title/subtitle 命令。
     *
     * @return 失败说明；成功返回 null
     */
    private fun sendComponentCommand(commandPrefix: String, component: String?): String? {
        if (component == null) {
            return null
        }
        val text = buildText(component, applyPrefix = false)
        if (text.isEmpty()) {
            return null
        }
        val result = sendWithLimit(commandPrefix, text)
        return if (result.isSuccess) null else result.message ?: "命令发送失败"
    }

    private fun buildText(component: String?, applyPrefix: Boolean): String {
        val payload = component ?: ""
        val prefix = if (applyPrefix) prefixProvider() ?: "" else ""
        return sanitize(prefix + payload)
    }

    /**
     * 把文本限制在 RCON 命令字节上限内；超长时截断并按约定返回失败。
     */
    private fun sendWithLimit(commandPrefix: String, text: String): PlatformResult<Void> {
        val maxTextBytes = MAX_COMMAND_BYTES - commandPrefix.toByteArray(StandardCharsets.UTF_8).size
        if (maxTextBytes <= 0) {
            return PlatformResult.failure(PlatformResultCode.FAILED, "RCON 命令前缀过长")
        }

        val (body, truncated) = truncateUtf8(text, maxTextBytes)
        if (body.isEmpty()) {
            return PlatformResult.failure(PlatformResultCode.FAILED, "消息内容超出 RCON 长度限制")
        }

        val result = sendCommand(commandPrefix + body)
        if (result != null) {
            return PlatformResult.failure(PlatformResultCode.FAILED, result)
        }
        if (truncated) {
            return PlatformResult.failure(PlatformResultCode.FAILED, "消息超长，已截断发送")
        }
        return PlatformResult.success(null)
    }

    /**
     * @return 失败说明；成功返回 null
     */
    private fun sendCommand(command: String): String? {
        val current = runtime
            ?: return "QueQiao Runtime 未就绪"
        return try {
            val response = current.sendRconCommand(command)
            if (isCommandFailure(response)) {
                logger.warn("RCON 命令执行失败：{}，响应：{}", command, response)
                response
            } else {
                null
            }
        } catch (e: RconException) {
            logger.warn("RCON 命令发送失败：{}，原因：{}", command, e.message)
            e.message ?: "RCON 命令发送失败"
        } catch (e: RuntimeException) {
            logger.warn("RCON 命令发送异常：{}", command, e)
            e.message ?: "RCON 命令发送异常"
        }
    }

    private fun isCommandFailure(response: String?): Boolean {
        if (response.isNullOrBlank()) {
            return false
        }
        val lower = response.lowercase()
        return lower.contains("unknown command") ||
            lower.contains("unknown or incomplete command") ||
            lower.contains("incorrect argument") ||
            lower.contains("expected ")
    }

    private fun sanitize(text: String): String {
        return text
            .replace(Regex("[\\r\\n\\t]+"), " ")
            .replace(Regex("[\\u0000-\\u001F\\u007F]+"), "")
            .replace(Regex("§[0-9A-FK-ORa-fk-orxX]"), "")
            .replace(Regex(" {2,}"), " ")
            .trim()
    }

    companion object {

        /**
         * Minecraft RCON EXEC_COMMAND 数据包可承载的最大命令字节数。
         */
        private const val MAX_COMMAND_BYTES = 1446

        /**
         * 按 UTF-8 字节截断，且不切断多字节字符。
         */
        fun truncateUtf8(text: String, maxBytes: Int): Pair<String, Boolean> {
            val bytes = text.toByteArray(StandardCharsets.UTF_8)
            if (bytes.size <= maxBytes) {
                return text to false
            }

            var end = maxBytes
            while (end > 0 && (bytes[end].toInt() and 0xC0) == 0x80) {
                end--
            }
            return String(bytes, 0, end, StandardCharsets.UTF_8) to true
        }
    }
}

/**
 * 把 MC 的 JSON 文本组件压平成纯文本，忽略颜色、点击事件等格式信息。
 */
object JsonComponentText {

    fun flatten(element: JsonElement?): String {
        if (element == null || element.isJsonNull) {
            return ""
        }
        if (element.isJsonPrimitive) {
            return element.asString
        }
        if (element.isJsonArray) {
            return element.asJsonArray.joinToString("") { flatten(it) }
        }
        if (element.isJsonObject) {
            val obj = element.asJsonObject
            val builder = StringBuilder()
            if (obj.has("text") && !obj.get("text").isJsonNull) {
                builder.append(obj.get("text").asString)
            } else if (obj.has("translate") && !obj.get("translate").isJsonNull) {
                builder.append(obj.get("translate").asString)
            }
            if (obj.has("extra")) {
                builder.append(flatten(obj.get("extra")))
            }
            return builder.toString()
        }
        return element.toString()
    }
}
