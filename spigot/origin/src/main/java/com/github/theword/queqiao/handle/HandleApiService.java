package com.github.theword.queqiao.handle;


import com.github.theword.queqiao.tool.handle.HandleApi;
import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonSendTitle;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.instance;


public class HandleApiService implements HandleApi {

    private final ParseJsonToEvent parseJsonToEvent = new ParseJsonToEvent();

    @Override
    public void handleBroadcastMessage(WebSocket webSocket, List<CommonTextComponent> messageList) {
        TextComponent textComponent = parseJsonToEvent.parsePerMessageToTextComponent(Tool.getPrefixComponent());
        textComponent.addExtra(parseJsonToEvent.parseMessageToTextComponent(messageList));
        instance.getServer().spigot().broadcast(textComponent);
    }

    @Override
    public void handleSendTitleMessage(WebSocket webSocket, CommonSendTitle sendTitle) {
        String title = parseJsonToEvent.parseCommonBaseCommentToStringWithStyle(sendTitle.getTitle());
        String subtitle = parseJsonToEvent.parseCommonBaseCommentToStringWithStyle(sendTitle.getSubtitle());
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.sendTitle(
                    title,
                    subtitle,
                    sendTitle.getFadein(),
                    sendTitle.getStay(),
                    sendTitle.getFadeout()
            );
        }
    }

    /**
     * 私聊消息
     *
     * @param webSocket websocket
     * @param targetPlayerName 目标玩家名称
     * @param targetPlayerUuid 目标玩家 UUID
     * @param messageList 消息体
     */
    @Override
    public void handlePrivateMessage(WebSocket webSocket, String targetPlayerName, UUID targetPlayerUuid, List<CommonTextComponent> messageList) {
        webSocket.send("Unsupported API now.");
    }

    @Override
    public void handleActionBarMessage(WebSocket webSocket, List<CommonBaseComponent> messageList) {
        TextComponent actionTextComponent = parseJsonToEvent.parseMessageToTextComponent(messageList);
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionTextComponent);
        }
    }
}