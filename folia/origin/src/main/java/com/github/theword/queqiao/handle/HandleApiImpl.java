package com.github.theword.queqiao.handle;


import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.TextComponent;


import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.instance;
import static com.github.theword.queqiao.utils.FoliaTool.getFoliaPlayer;


public class HandleApiImpl implements HandleApiService {

    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        TextComponent textComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        textComponent = textComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        for (Player player : instance.getServer().getOnlinePlayers()) {
            if (player.isOnline()) player.sendMessage(textComponent);
        }
    }


    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        TextComponent titleComponent = parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle());
        TextComponent subtitleComponent = Component.empty();
        if (titlePayload.getSubtitle() != null)
            subtitleComponent = parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle());

        Title title = Title.title(
                titleComponent,
                subtitleComponent,
                Title.Times.times(
                        Duration.ofMillis(titlePayload.getFadein()),
                        Duration.ofMillis(titlePayload.getStay()),
                        Duration.ofMillis(titlePayload.getFadeout())
                )
        );
        for (Player player : instance.getServer().getOnlinePlayers()) {
            if (player.isOnline()) player.showTitle(title);
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
        else return PrivateMessageResponse.playerNotFound();

        if (targetPlayer == null) return PrivateMessageResponse.playerIsNull();

        if (!targetPlayer.isOnline()) return PrivateMessageResponse.playerNotOnline();

        TextComponent textComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        textComponent = textComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        targetPlayer.sendMessage(textComponent);
        return PrivateMessageResponse.sendSuccess(getFoliaPlayer(targetPlayer));
    }

    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        TextComponent actionTextComponent = parseJsonToEventImpl.parseMessageListToComponent(messageList);
        for (Player player : instance.getServer().getOnlinePlayers()) {
            if (player.isOnline()) player.sendActionBar(actionTextComponent);
        }
    }
}
