package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.EventProcessor;
import com.github.theword.queqiao.config.Config;
import com.github.theword.queqiao.config.RegexConfig;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;

import static com.github.theword.queqiao.tool.utils.Tool.logger;

@RequiredArgsConstructor
public class LineProcessor {
    private final Config config;
    private final EventProcessor eventProcessor;

    public void processLine(String line) {
        logger.debug("LogWatcher: " + line);
        // 遍历并匹配聊天消息
        for (RegexConfig chatConfig : config.getChatRegexSet()) {
            Matcher chatMatcher = chatConfig.getPattern().matcher(line);
            if (chatMatcher.find()) {
                String playerName = chatMatcher.group(chatConfig.getPlayerGroup());
                String message = chatMatcher.group(chatConfig.getMessageGroup());

                // 检查聊天内容是否存在
                if (message != null && !message.isEmpty()) {
                    logger.info("[Chat] " + playerName + ": " + message);
                    eventProcessor.onPlayerChat(playerName, message);
                }
                return;
            }
        }

        // 遍历并匹配玩家加入消息
        for (RegexConfig joinConfig : config.getJoinRegexSet()) {
            Matcher joinMatcher = joinConfig.getPattern().matcher(line);
            if (joinMatcher.find()) {
                logger.info("[Join] " + line);
                String playerName = joinMatcher.group(joinConfig.getPlayerGroup());
                eventProcessor.onPlayerJoin(playerName);
                return;
            }
        }

        // 遍历并匹配玩家退出消息
        for (RegexConfig quitConfig : config.getQuitRegexSet()) {
            Matcher quitMatcher = quitConfig.getPattern().matcher(line);
            if (quitMatcher.find()) {
                logger.info("[Quit] " + line);
                String playerName = quitMatcher.group(quitConfig.getPlayerGroup());
                eventProcessor.onPlayerQuit(playerName);
                return;
            }
        }
    }
}
