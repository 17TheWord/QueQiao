package com.github.theword.queqiao.utils

import com.github.theword.queqiao.EventProcessor
import com.github.theword.queqiao.config.Config
import org.slf4j.Logger

class LineProcessor(
    private val regexConfig: Config,
    private val config: com.github.theword.queqiao.tool.config.Config,
    private val logger: Logger,
    private val eventProcessor: EventProcessor
) {

    fun processLine(line: String) {
        logger.debug("LogWatcher: $line")
        if (config.subscribeEvent.isPlayerChat)
            regexConfig.chatRegexSet.forEach { chatConfig ->
                val chatMatcher = chatConfig.pattern.matcher(line)
                if (chatMatcher.find()) {
                    val playerName = chatMatcher.group(chatConfig.playerGroup)
                    val message = chatMatcher.group(chatConfig.messageGroup!!)
                    if (message != null && message.isNotEmpty()) {
                        logger.info("[Chat] $playerName: $message")
                        eventProcessor.onPlayerChat(playerName, message)
                    }
                    return
                }
            }

        if (config.subscribeEvent.isPlayerJoin)
            regexConfig.joinRegexSet.forEach { joinConfig ->
                val joinMatcher = joinConfig.pattern.matcher(line)
                if (joinMatcher.find()) {
                    logger.info("[Join] $line")
                    val playerName = joinMatcher.group(joinConfig.playerGroup)
                    eventProcessor.onPlayerJoin(playerName)
                    return
                }
            }

        if (config.subscribeEvent.isPlayerQuit)
            regexConfig.quitRegexSet.forEach { quitConfig ->
                val quitMatcher = quitConfig.pattern.matcher(line)
                if (quitMatcher.find()) {
                    logger.info("[Quit] $line")
                    val playerName = quitMatcher.group(quitConfig.playerGroup)
                    eventProcessor.onPlayerQuit(playerName)
                    return
                }
            }
    }
}