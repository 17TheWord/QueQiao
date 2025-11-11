package com.github.theword.queqiao.handle;


import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.PaperTool;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.instance;


public class HandleApiImpl implements HandleApiService {


    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        Component prefix = PaperTool.buildComponent(Tool.getPrefixComponent());
        Component message = PaperTool.buildComponent(jsonElement);
        Component result = prefix.append(message);
        instance.getServer().sendMessage(result);
    }

    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        Component title = Component.empty();
        Component subtitle = Component.empty();
        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
            title = PaperTool.buildComponent(titleJsonElement);
        }
        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
            subtitle = PaperTool.buildComponent(subtitleJsonElement);
        }
        Title.Times times = Title.Times.of(Ticks.duration(fadein), Ticks.duration(stay), Ticks.duration(fadeout));
        instance.getServer().showTitle(Title.title(title, subtitle, times));
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
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (!targetPlayer.isOnline()) {
            return PrivateMessageResponse.playerNotOnline();
        }

        Component prefix = PaperTool.buildComponent(Tool.getPrefixComponent());
        Component message = PaperTool.buildComponent(jsonElement);

        Component textComponent = prefix.append(message);
        targetPlayer.sendMessage(textComponent);
        return PrivateMessageResponse.sendSuccess(PaperTool.getPaperPlayer(targetPlayer));
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        Component message = PaperTool.buildComponent(jsonElement);
        instance.getServer().sendActionBar(message);
    }
}