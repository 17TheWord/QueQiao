package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.java_websocket.WebSocket;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;

public class HandleApiImpl implements HandleApiService {

    private final ParseJsonToEventImpl parseJsonToEvent = new ParseJsonToEventImpl();

    @Override
    public void handleBroadcastMessage(WebSocket webSocket, List<CommonTextComponent> messageList) {
        Component component = parseJsonToEvent.parsePerMessageToComponent(Tool.getPrefixComponent());
        Component append = component.append(parseJsonToEvent.parseMessageListToComponent(messageList));
        minecraftServer.sendMessage(append);
    }

    @Override
    public void handleSendTitleMessage(WebSocket webSocket, TitlePayload titlePayload) {
        Component title = parseJsonToEvent.parseMessageListToComponent(titlePayload.getTitle());
        minecraftServer.sendTitlePart(TitlePart.TITLE, title);
        if (titlePayload.getSubtitle() != null) {
            Component subtitle = parseJsonToEvent.parseMessageListToComponent(titlePayload.getSubtitle());
            minecraftServer.sendTitlePart(TitlePart.SUBTITLE, subtitle);
        }
    }

    @Override
    public void handleActionBarMessage(WebSocket webSocket, List<CommonTextComponent> messageList) {
        Component component = parseJsonToEvent.parsePerMessageToComponent(Tool.getPrefixComponent());
        minecraftServer.sendActionBar(component);
    }

    @Override
    public void handlePrivateMessage(WebSocket webSocket, String targetPlayerName, UUID targetPlayerUuid, List<CommonTextComponent> messageList) {
        for (Player player : minecraftServer.getAllPlayers()) {
            if ((targetPlayerUuid != null && targetPlayerUuid.equals(player.getUniqueId())) || (targetPlayerName != null && targetPlayerName.equals(player.getUsername()))) {
                Component component = parseJsonToEvent.parsePerMessageToComponent(Tool.getPrefixComponent());
                minecraftServer.sendMessage(component);
                webSocket.send("{ \"result\": 200, \"message\": \"Message sent\" }");
                break;
            }
        }
        webSocket.send("{ \"result\": 404, \"message\": \"Player not found\" }");
    }
}