package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.google.gson.JsonElement;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.VelocityTool.getVelocityPlayer;

public class HandleApiImpl implements HandleApiService {


    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        Component component = GsonComponentSerializer.gson().deserializeFromTree(Tool.getPrefixComponent());
        component = component.append(GsonComponentSerializer.gson().deserializeFromTree(jsonElement));
        minecraftServer.sendMessage(component);
    }

    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        Component titleComponent = Component.empty();
        Component subtitleComponent = Component.empty();
        if (titleJsonElement != null && titleJsonElement.isJsonNull()) {
            titleComponent = GsonComponentSerializer.gson().deserializeFromTree(titleJsonElement);
        }
        if (subtitleJsonElement != null && subtitleJsonElement.isJsonNull()) {
            subtitleComponent = GsonComponentSerializer.gson().deserializeFromTree(subtitleJsonElement);
        }
        Title title = Title.title(
                titleComponent,
                subtitleComponent,
                Title.Times.times(
                        Duration.ofMillis(fadein),
                        Duration.ofMillis(stay),
                        Duration.ofMillis(fadeout)
                )
        );
        minecraftServer.showTitle(title);
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        Component component = GsonComponentSerializer.gson().deserializeFromTree(jsonElement);
        minecraftServer.sendActionBar(component);
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
        for (Player player : minecraftServer.getAllPlayers()) {
            if ((uuid != null && uuid.equals(player.getUniqueId())) || (nickname != null && nickname.equals(player.getUsername()))) {
                Component component = GsonComponentSerializer.gson().deserializeFromTree(Tool.getPrefixComponent());
                component = component.append(GsonComponentSerializer.gson().deserializeFromTree(jsonElement));
                minecraftServer.sendMessage(component);
                return PrivateMessageResponse.sendSuccess(getVelocityPlayer(player));
            }
        }
        return PrivateMessageResponse.playerNotFound();
    }
}