package com.github.theword.queqiao.config

import io.github.theword.queqiao.core.constant.BaseConstant
import org.slf4j.Logger
import org.yaml.snakeyaml.Yaml
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * 独立端自有配置：regex.yml。
 *
 * <p>core 只管它自己的 config.yml；日志路径与正则仍然独立维护。
 */
class Config(
    val logPath: String,
    val charset: Charset,
    val chatRegexSet: List<RegexConfig>,
    val joinRegexSet: List<RegexConfig>,
    val quitRegexSet: List<RegexConfig>
) {

    companion object {

        private const val DEFAULT_LOG_PATH = "./logs/latest.log"

        fun load(logger: Logger): Config {
            val configPath = Paths.get("config", BaseConstant.MODULE_NAME, "regex.yml")
            ensureDefaultFile(configPath, logger)

            val configMap = try {
                Files.newInputStream(configPath).use { input ->
                    val loaded = Yaml().load<Any?>(input)
                    loaded as? Map<*, *>
                }
            } catch (e: Exception) {
                logger.warn("读取配置文件 {} 失败，将使用默认值：{}", configPath, e.message)
                null
            }

            val logPath = (configMap?.get("log_path") as? String)?.takeIf { it.isNotBlank() }
                ?: DEFAULT_LOG_PATH
            val charset = parseCharset(configMap?.get("log_encoding"), logger)

            return Config(
                logPath = logPath,
                charset = charset,
                chatRegexSet = parseRegexSet(configMap?.get("chat_regex"), "chat_regex", logger),
                joinRegexSet = parseRegexSet(configMap?.get("join_regex"), "join_regex", logger),
                quitRegexSet = parseRegexSet(configMap?.get("quit_regex"), "quit_regex", logger)
            )
        }

        private fun ensureDefaultFile(configPath: Path, logger: Logger) {
            if (Files.exists(configPath)) {
                return
            }
            try {
                val input = Config::class.java.classLoader.getResourceAsStream("queqiao/regex.yml")
                if (input == null) {
                    logger.warn("默认配置模板 queqiao/regex.yml 不存在")
                    return
                }
                input.use {
                    configPath.parent?.let { parent -> Files.createDirectories(parent) }
                    Files.copy(it, configPath, StandardCopyOption.REPLACE_EXISTING)
                }
                logger.info("已生成默认配置文件 {}", configPath)
            } catch (e: Exception) {
                logger.warn("生成默认配置文件 {} 失败：{}", configPath, e.message)
            }
        }

        private fun parseCharset(value: Any?, logger: Logger): Charset {
            val name = value as? String
            if (name.isNullOrBlank()) {
                return StandardCharsets.UTF_8
            }
            return try {
                Charset.forName(name)
            } catch (e: Exception) {
                logger.warn("log_encoding 配置非法（{}），将使用 UTF-8", name)
                StandardCharsets.UTF_8
            }
        }

        private fun parseRegexSet(value: Any?, key: String, logger: Logger): List<RegexConfig> {
            if (value !is List<*>) {
                if (value != null) {
                    logger.warn("配置项 {} 不是列表类型，将使用空集合。", key)
                }
                return emptyList()
            }
            return value.mapNotNull { item ->
                if (item !is Map<*, *>) {
                    logger.warn("配置项 {} 中包含非 Map 类型，将跳过该值。", key)
                    return@mapNotNull null
                }
                val regex = item["regex"] as? String
                val playerGroup = (item["player_group"] as? Number)?.toInt()
                val messageGroup = (item["message_group"] as? Number)?.toInt()
                if (regex == null || playerGroup == null) {
                    logger.warn("配置项 {} 中包含无效的 Map，将跳过该值。", key)
                    null
                } else {
                    RegexConfig(regex, playerGroup, messageGroup)
                }
            }
        }
    }
}
