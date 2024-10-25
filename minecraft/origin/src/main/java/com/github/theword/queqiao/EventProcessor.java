package com.github.theword.queqiao;

import com.github.theword.queqiao.event.minecraft.MinecraftPlayer;
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerChatEvent;
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerJoinEvent;
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerQuitEvent;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.tool.utils.Tool.sendWebsocketMessage;

public class EventProcessor {
    /**
     * 监听玩家聊天
     */
    public void onPlayerChat(String playerName, String message) {
        if (!config.getSubscribeEvent().isPlayerChat()) return;
        MinecraftPlayer player = new MinecraftPlayer(playerName);
        MinecraftPlayerChatEvent event = new MinecraftPlayerChatEvent("", player, message);
        sendWebsocketMessage(event);
    }


    /**
     * 监听玩家加入事件
     */
    public void onPlayerJoin(String playerName) {
        if (!config.getSubscribeEvent().isPlayerJoin()) return;
        MinecraftPlayer player = new MinecraftPlayer(playerName);
        MinecraftPlayerJoinEvent event = new MinecraftPlayerJoinEvent(player);
        sendWebsocketMessage(event);
    }

    /**
     * 监听玩家离开事件
     */
    public void onPlayerQuit(String playerName) {
        if (!config.getSubscribeEvent().isPlayerQuit()) return;
        MinecraftPlayer player = new MinecraftPlayer(playerName);
        MinecraftPlayerQuitEvent event = new MinecraftPlayerQuitEvent(player);
        sendWebsocketMessage(event);
    }
}
