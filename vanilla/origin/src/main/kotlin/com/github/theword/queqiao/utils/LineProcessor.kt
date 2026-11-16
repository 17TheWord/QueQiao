package com.github.theword.queqiao.utils

import com.github.theword.queqiao.EventProcessor
import com.github.theword.queqiao.config.Config
import io.github.theword.queqiao.core.config.ConfigKeys
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import org.slf4j.Logger

class LineProcessor(
    private val regexConfig: Config,
    private val runtime: QueQiaoRuntime,
    private val logger: Logger,
    private val eventProcessor: EventProcessor
) {

    fun processLine(line: String) {
        logger.debug("LogWatcher: {}", line)

        if (runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) {
            regexConfig.chatRegexSet.forEach { chatConfig ->
                val chatMatcher = chatConfig.pattern.matcher(line)
                if (chatMatcher.find()) {
                    val playerName = chatMatcher.group(chatConfig.playerGroup)
                    val message = chatConfig.messageGroup?.let { chatMatcher.group(it) }
                    if (message != null && message.isNotEmpty()) {
                        logger.info("[Chat] {}: {}", playerName, message)
                        eventProcessor.onPlayerChat(playerName, message)
                    }
                    return
                }
            }
        }

        if (runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) {
            regexConfig.joinRegexSet.forEach { joinConfig ->
                val joinMatcher = joinConfig.pattern.matcher(line)
                if (joinMatcher.find()) {
                    logger.info("[Join] {}", line)
                    eventProcessor.onPlayerJoin(joinMatcher.group(joinConfig.playerGroup))
                    return
                }
            }
        }

        if (runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) {
            regexConfig.quitRegexSet.forEach { quitConfig ->
                val quitMatcher = quitConfig.pattern.matcher(line)
                if (quitMatcher.find()) {
                    logger.info("[Quit] {}", line)
                    eventProcessor.onPlayerQuit(quitMatcher.group(quitConfig.playerGroup))
                    return
                }
            }
        }
    }
}
