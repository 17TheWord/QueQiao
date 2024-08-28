package com.github.theword.queqiao;

import com.github.theword.queqiao.event.spigot.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.SpigotTool.getSpigotPlayer;


class EventProcessor implements Listener {
    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler
    void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || !config.getSubscribeEvent().isPlayerChat()) return;

        SpigotAsyncPlayerChatEvent spigotAsyncPlayerChatEvent = new SpigotAsyncPlayerChatEvent(getSpigotPlayer(event.getPlayer()), event.getMessage());
        sendWebsocketMessage(spigotAsyncPlayerChatEvent);
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        if (!config.getSubscribeEvent().isPlayerDeath()) return;

        SpigotPlayerDeathEvent spigotPlayerDeathEvent = new SpigotPlayerDeathEvent(getSpigotPlayer(event.getEntity()), event.getDeathMessage());
        sendWebsocketMessage(spigotPlayerDeathEvent);
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        if (!config.getSubscribeEvent().isPlayerJoin()) return;

        SpigotPlayerJoinEvent spigotPlayerJoinEvent = new SpigotPlayerJoinEvent(getSpigotPlayer(event.getPlayer()));
        sendWebsocketMessage(spigotPlayerJoinEvent);
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        if (!config.getSubscribeEvent().isPlayerQuit()) return;

        SpigotPlayerQuitEvent spigotPlayerQuitEvent = new SpigotPlayerQuitEvent(getSpigotPlayer(event.getPlayer()));
        sendWebsocketMessage(spigotPlayerQuitEvent);
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
    @EventHandler
    void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!config.getSubscribeEvent().isPlayerCommand()) return;

        String command = isRegisterOrLoginCommand(event.getMessage());

        if (command.isEmpty()) return;

        SpigotPlayerCommandPreprocessEvent spigotPlayerCommandPreprocessEvent = new SpigotPlayerCommandPreprocessEvent(getSpigotPlayer(event.getPlayer()), command);
        sendWebsocketMessage(spigotPlayerCommandPreprocessEvent);
    }
}