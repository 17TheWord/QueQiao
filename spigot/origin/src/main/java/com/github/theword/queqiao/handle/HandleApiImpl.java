package com.github.theword.queqiao.handle;


import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.SpigotTool;
import com.google.gson.JsonElement;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.instance;
import static com.github.theword.queqiao.utils.SpigotTool.getSpigotPlayer;


public class HandleApiImpl implements HandleApiService {


    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        BaseComponent[] prefix = SpigotTool.buildComponent(Tool.getPrefixComponent());
        BaseComponent[] message = SpigotTool.buildComponent(jsonElement);

        BaseComponent textComponent = new TextComponent();
        textComponent.addExtra(prefix[0]);

        for (BaseComponent base : message) {
            textComponent.addExtra(base);
        }

        instance.getServer().spigot().broadcast(textComponent);
    }

    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        String titleText = "";
        if (!titleJsonElement.isJsonNull()) {
            BaseComponent[] title = SpigotTool.buildComponent(titleJsonElement);
            titleText = TextComponent.toLegacyText(title);
        }
        String subtitleText = "";
        if (!subtitleJsonElement.isJsonNull()) {
            BaseComponent[] subtitle = SpigotTool.buildComponent(subtitleJsonElement);
            subtitleText = TextComponent.toLegacyText(subtitle);
        }
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.sendTitle(
                    titleText,
                    subtitleText,
                    fadein,
                    stay,
                    fadeout
            );
        }
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

        BaseComponent[] prefix = SpigotTool.buildComponent(Tool.getPrefixComponent());
        BaseComponent[] message = SpigotTool.buildComponent(jsonElement);

        BaseComponent textComponent = new TextComponent();
        textComponent.addExtra(prefix[0]);

        for (BaseComponent base : message) {
            textComponent.addExtra(base);
        }

        targetPlayer.sendMessage(TextComponent.toLegacyText(textComponent));
        return PrivateMessageResponse.sendSuccess(getSpigotPlayer(targetPlayer));
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        BaseComponent[] message = SpigotTool.buildComponent(jsonElement);
        for (Player player : instance.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        }
    }
}