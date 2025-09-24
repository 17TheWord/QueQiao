package com.github.theword.queqiao.config

import com.github.theword.queqiao.tool.GlobalContext
import com.github.theword.queqiao.tool.config.CommonConfig


class Config : CommonConfig() {
    var logPath: String = ".\\logs\\latest.log"
    var chatRegexSet: Set<RegexConfig> = emptySet()
    var joinRegexSet: Set<RegexConfig> = emptySet()
    var quitRegexSet: Set<RegexConfig> = emptySet()

    init {
        readConfigFile("", "regex.yml")
    }

    override fun loadConfigValues(configMap: Map<String, Any>) {
        logPath = configMap["log_path"] as String? ?: "./logs/latest.log"
        chatRegexSet = convertToRegexConfigSet(configMap, "chat_regex")
        joinRegexSet = convertToRegexConfigSet(configMap, "join_regex")
        quitRegexSet = convertToRegexConfigSet(configMap, "quit_regex")
    }

    private fun convertToRegexConfigSet(configMap: Map<String, Any>, key: String): Set<RegexConfig> {
        val value = configMap[key]
        if (value !is List<*>) {
            GlobalContext.getLogger().warn("配置项 {} 不是列表类型，将使用空集合。", key)
            return emptySet()
        }
        return value.mapNotNull { item ->
            if (item is Map<*, *>) {
                val regex = item["regex"] as? String
                val playerGroup = item["player_group"] as? Int
                val messageGroup = item["message_group"] as? Int
                if (regex != null && playerGroup != null) {
                    RegexConfig(regex, playerGroup, messageGroup)
                } else {
                    GlobalContext.getLogger().warn("配置项 {} 中包含无效的 Map，将跳过该值。", key)
                    null
                }
            } else {
                GlobalContext.getLogger().warn("配置项 {} 中包含非Map类型，将跳过该值。", key)
                null
            }
        }.toSet()
    }
}