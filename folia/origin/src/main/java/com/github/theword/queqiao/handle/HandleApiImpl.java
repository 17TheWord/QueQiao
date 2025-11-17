package com.github.theword.queqiao.handle;


import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;


import java.time.Duration;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.instance;
import static com.github.theword.queqiao.utils.FoliaTool.getFoliaPlayer;


public class HandleApiImpl implements HandleApiService {


    /**
     * 广播消息
     *
     * @param jsonElement 消息体
     */
    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        Component component = GsonComponentSerializer.gson().deserializeFromTree(GlobalContext.getMessagePrefixJsonObject());
        component = component.append(GsonComponentSerializer.gson().deserializeFromTree(jsonElement));
        instance.getServer().broadcast(component);
    }


    /**
     * 标题消息
     *
     * @param titleJsonElement    title
     * @param subtitleJsonElement subtitle
     * @param fadein              fadein
     * @param stay                stay
     * @param fadeout             fadeout
     */
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
        instance.getServer().showTitle(title);
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param jsonElement 消息体
     */
    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
        Player targetPlayer;
        if (uuid != null)
            targetPlayer = instance.getServer().getPlayer(uuid);
        else if (nickname != null && !nickname.isEmpty())
            targetPlayer = instance.getServer().getPlayer(nickname);
        else return PrivateMessageResponse.playerNotFound();

        if (targetPlayer == null) return PrivateMessageResponse.playerIsNull();

        if (!targetPlayer.isOnline()) return PrivateMessageResponse.playerNotOnline();

        Component component = GsonComponentSerializer.gson().deserializeFromTree(GlobalContext.getMessagePrefixJsonObject());
        component = component.append(GsonComponentSerializer.gson().deserializeFromTree(jsonElement));
        targetPlayer.sendMessage(component);
        return PrivateMessageResponse.sendSuccess(getFoliaPlayer(targetPlayer));
    }

    /**
     * 动作栏消息
     *
     * @param jsonElement 消息体s
     */
    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        Component component = GsonComponentSerializer.gson().deserializeFromTree(jsonElement);
        instance.getServer().sendActionBar(component);
    }
}