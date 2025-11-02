package com.github.theword.queqiao;

import com.github.theword.queqiao.event.spigot.*;

import com.github.theword.queqiao.event.spigot.dto.advancement.SpigotAdvancement;
import com.github.theword.queqiao.tool.GlobalContext;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;


import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
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
        if (event.isCancelled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        SpigotAsyncPlayerChatEvent spigotAsyncPlayerChatEvent = new SpigotAsyncPlayerChatEvent(getSpigotPlayer(event.getPlayer()), event.getMessage());
        GlobalContext.sendEvent(spigotAsyncPlayerChatEvent);
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        SpigotPlayerDeathEvent spigotPlayerDeathEvent = new SpigotPlayerDeathEvent(getSpigotPlayer(event.getEntity()), event.getDeathMessage());
        GlobalContext.sendEvent(spigotPlayerDeathEvent);
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        SpigotPlayerJoinEvent spigotPlayerJoinEvent = new SpigotPlayerJoinEvent(getSpigotPlayer(event.getPlayer()));
        GlobalContext.sendEvent(spigotPlayerJoinEvent);
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        SpigotPlayerQuitEvent spigotPlayerQuitEvent = new SpigotPlayerQuitEvent(getSpigotPlayer(event.getPlayer()));
        GlobalContext.sendEvent(spigotPlayerQuitEvent);
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
    @EventHandler
    void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        String command = isRegisterOrLoginCommand(event.getMessage());

        if (command.isEmpty()) return;

        SpigotPlayerCommandPreprocessEvent spigotPlayerCommandPreprocessEvent = new SpigotPlayerCommandPreprocessEvent(getSpigotPlayer(event.getPlayer()), command);
        GlobalContext.sendEvent(spigotPlayerCommandPreprocessEvent);
    }

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement();

        SpigotAdvancement spigotAdvancement = getSpigotAdvancement(advancement);

        SpigotPlayerAdvancementDoneEvent spigotPlayerAdvancementDoneEvent = new SpigotPlayerAdvancementDoneEvent(getSpigotPlayer(event.getPlayer()), spigotAdvancement);
        GlobalContext.sendEvent(spigotPlayerAdvancementDoneEvent);
    }


}