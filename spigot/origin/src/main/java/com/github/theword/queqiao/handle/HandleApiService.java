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
        TextComponent title = parseJsonToEvent.parseMessageToTextComponent(sendTitle.getTitle());
        TextComponent subtitle = parseJsonToEvent.parseMessageToTextComponent(sendTitle.getSubtitle());
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.sendTitle(
                    title.toLegacyText(),
                    subtitle.toLegacyText(),
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
        Player targetPlayer;
        if (targetPlayerUuid != null)
            targetPlayer = instance.getServer().getPlayer(targetPlayerUuid);
        else if (targetPlayerName != null && !targetPlayerName.isEmpty())
            targetPlayer = instance.getServer().getPlayer(targetPlayerName);
        else {
            webSocket.send("{\"code\":400,\"message\":\"Target player not found.\"}");
            return;
        }

        if (targetPlayer == null) {
            webSocket.send("{\"code\":400,\"message\":\"Target player is null.\"}");
            return;
        }

        if (!targetPlayer.isOnline()) {
            webSocket.send("{\"code\":400,\"message\":\"Target player is offline.\"}");
            return;
        }

        TextComponent textComponent = parseJsonToEvent.parsePerMessageToTextComponent(Tool.getPrefixComponent());
        textComponent.addExtra(parseJsonToEvent.parseMessageToTextComponent(messageList));
        targetPlayer.sendMessage(textComponent.toLegacyText());
        webSocket.send("{\"code\":200,\"message\":\"Private message sent.\"}");
    }

    @Override
    public void handleActionBarMessage(WebSocket webSocket, List<CommonBaseComponent> messageList) {
        TextComponent actionTextComponent = parseJsonToEvent.parseMessageToTextComponent(messageList);
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionTextComponent);
        }
    }
}