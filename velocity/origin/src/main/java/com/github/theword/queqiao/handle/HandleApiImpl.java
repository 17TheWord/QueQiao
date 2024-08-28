package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.VelocityTool.getVelocityPlayer;

public class HandleApiImpl implements HandleApiService {

    private final ParseJsonToEventImpl parseJsonToEvent = new ParseJsonToEventImpl();

    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        Component component = parseJsonToEvent.parsePerMessageToComponent(Tool.getPrefixComponent());
        Component append = component.append(parseJsonToEvent.parseMessageListToComponent(messageList));
        minecraftServer.sendMessage(append);
    }

    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        Component title = parseJsonToEvent.parseMessageListToComponent(titlePayload.getTitle());
        minecraftServer.sendTitlePart(TitlePart.TITLE, title);
        if (titlePayload.getSubtitle() != null) {
            Component subtitle = parseJsonToEvent.parseMessageListToComponent(titlePayload.getSubtitle());
            minecraftServer.sendTitlePart(TitlePart.SUBTITLE, subtitle);
        }
    }

    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        Component component = parseJsonToEvent.parsePerMessageToComponent(Tool.getPrefixComponent());
        minecraftServer.sendActionBar(component);
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, List<MessageSegment> messageList) {
        for (Player player : minecraftServer.getAllPlayers()) {
            if ((uuid != null && uuid.equals(player.getUniqueId())) || (nickname != null && nickname.equals(player.getUsername()))) {
                Component component = parseJsonToEvent.parsePerMessageToComponent(Tool.getPrefixComponent());
                minecraftServer.sendMessage(component);
                return PrivateMessageResponse.sendSuccess(getVelocityPlayer(player));
            }
        }
        return PrivateMessageResponse.playerNotFound();
    }
}