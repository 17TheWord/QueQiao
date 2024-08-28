package com.github.theword.queqiao.handle;


import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.instance;
import static com.github.theword.queqiao.utils.SpigotTool.getSpigotPlayer;


public class HandleApiImpl implements HandleApiService {

    private final ParseJsonToEventImpl parseJsonToEventService = new ParseJsonToEventImpl();

    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        TextComponent textComponent = parseJsonToEventService.parsePerMessageToComponent(Tool.getPrefixComponent());
        textComponent.addExtra(parseJsonToEventService.parseMessageListToComponent(messageList));
        instance.getServer().spigot().broadcast(textComponent);
    }

    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        TextComponent title = parseJsonToEventService.parseMessageListToComponent(titlePayload.getTitle());
        String subtitleText = "";
        if (titlePayload.getSubtitle() != null) {
            subtitleText = parseJsonToEventService.parseMessageListToComponent(titlePayload.getSubtitle()).toLegacyText();
        }
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.sendTitle(
                    title.toLegacyText(),
                    subtitleText,
                    titlePayload.getFadein(),
                    titlePayload.getStay(),
                    titlePayload.getFadeout()
            );
        }
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param messageList 消息体
     */
    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, List<MessageSegment> messageList) {
        Player targetPlayer;
        if (uuid != null)
            targetPlayer = instance.getServer().getPlayer(uuid);
        else if (nickname != null && !nickname.isEmpty())
            targetPlayer = instance.getServer().getPlayer(nickname);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (!targetPlayer.isOnline()) {
            return PrivateMessageResponse.playerNotOnline();
        }

        TextComponent textComponent = parseJsonToEventService.parsePerMessageToComponent(Tool.getPrefixComponent());
        textComponent.addExtra(parseJsonToEventService.parseMessageListToComponent(messageList));
        targetPlayer.sendMessage(textComponent.toLegacyText());
        return PrivateMessageResponse.sendSuccess(getSpigotPlayer(targetPlayer));
    }

    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        TextComponent actionTextComponent = parseJsonToEventService.parseMessageListToComponent(messageList);
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionTextComponent);
        }
    }
}