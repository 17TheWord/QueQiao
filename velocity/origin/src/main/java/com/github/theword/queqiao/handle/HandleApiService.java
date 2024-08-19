package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApi;
import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonSendTitle;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.java_websocket.WebSocket;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;

public class HandleApiService implements HandleApi {

    private final ParseJsonToEvent parseJsonToEvent = new ParseJsonToEvent();

    @Override
    public void handleBroadcastMessage(WebSocket webSocket, List<CommonTextComponent> messageList) {
        Component component = parseJsonToEvent.parsePerMessageToMultiText(Tool.getPrefixComponent());
        Component append = component.append(parseJsonToEvent.parseMessage(messageList));
        minecraftServer.sendMessage(append);
    }

    @Override
    public void handleSendTitleMessage(WebSocket webSocket, CommonSendTitle commonSendTitle) {
        Component title = parseJsonToEvent.parseMessage(commonSendTitle.getTitle());
        minecraftServer.sendTitlePart(TitlePart.TITLE, title);
        if (commonSendTitle.getSubtitle() != null) {
            Component subtitle = parseJsonToEvent.parseMessage(commonSendTitle.getSubtitle());
            minecraftServer.sendTitlePart(TitlePart.SUBTITLE, subtitle);
        }
    }

    @Override
    public void handleActionBarMessage(WebSocket webSocket, List<CommonBaseComponent> messageList) {
        Component component = parseJsonToEvent.parsePerMessageToMultiText(Tool.getPrefixComponent());
        minecraftServer.sendActionBar(component);
    }

    @Override
    public void handlePrivateMessage(WebSocket webSocket, String targetPlayerName, UUID targetPlayerUuid, List<CommonTextComponent> messageList) {
        for (Player player : minecraftServer.getAllPlayers()) {
            if ((targetPlayerUuid != null && targetPlayerUuid.equals(player.getUniqueId())) || (targetPlayerName != null && targetPlayerName.equals(player.getUsername()))) {
                Component component = parseJsonToEvent.parsePerMessageToMultiText(Tool.getPrefixComponent());
                minecraftServer.sendMessage(component);
                webSocket.send("{ \"result\": 200, \"message\": \"Message sent\" }");
                break;
            }
        }
        webSocket.send("{ \"result\": 404, \"message\": \"Player not found\" }");
    }
}