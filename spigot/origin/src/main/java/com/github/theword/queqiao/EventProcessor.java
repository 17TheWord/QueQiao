package com.github.theword.queqiao;

import com.github.theword.queqiao.event.spigot.*;

import com.github.theword.queqiao.event.spigot.dto.advancement.SpigotAdvancement;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;


import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.SpigotTool.getSpigotPlayer;
import static com.github.theword.queqiao.utils.SpigotTool.getSpigotAdvancement;


class EventProcessor implements Listener {
    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
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

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        if (!config.getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement();

        SpigotAdvancement spigotAdvancement = getSpigotAdvancement(advancement);

        SpigotPlayerAdvancementDoneEvent spigotPlayerAdvancementDoneEvent = new SpigotPlayerAdvancementDoneEvent(getSpigotPlayer(event.getPlayer()), spigotAdvancement);
        sendWebsocketMessage(spigotPlayerAdvancementDoneEvent);
    }


}