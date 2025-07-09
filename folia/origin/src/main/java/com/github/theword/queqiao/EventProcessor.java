package com.github.theword.queqiao;

import com.github.theword.queqiao.event.folia.*;

import com.github.theword.queqiao.event.folia.dto.advancement.FoliaAdvancement;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import io.papermc.paper.event.player.AsyncChatEvent;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.FoliaTool.*;


class EventProcessor implements Listener {
    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerChat(AsyncChatEvent event) {
        if (event.isCancelled() || !config.getSubscribeEvent().isPlayerChat()) return;

        FoliaAsyncPlayerChatEvent foliaAsyncPlayerChatEvent = new FoliaAsyncPlayerChatEvent(getFoliaPlayer(event.getPlayer()), getComponentText(event.message()));
        sendWebsocketMessage(foliaAsyncPlayerChatEvent);
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        if (!config.getSubscribeEvent().isPlayerDeath()) return;
        Component component = event.deathMessage();
        if (component == null) return;

        String string = getComponentText(component);
        FoliaPlayerDeathEvent foliaPlayerDeathEvent = new FoliaPlayerDeathEvent(getFoliaPlayer(event.getEntity()), string);
        sendWebsocketMessage(foliaPlayerDeathEvent);
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        if (!config.getSubscribeEvent().isPlayerJoin()) return;

        FoliaPlayerJoinEvent foliaPlayerJoinEvent = new FoliaPlayerJoinEvent(getFoliaPlayer(event.getPlayer()));
        sendWebsocketMessage(foliaPlayerJoinEvent);
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        if (!config.getSubscribeEvent().isPlayerQuit()) return;

        FoliaPlayerQuitEvent foliaPlayerQuitEvent = new FoliaPlayerQuitEvent(getFoliaPlayer(event.getPlayer()));
        sendWebsocketMessage(foliaPlayerQuitEvent);
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

        FoliaPlayerCommandPreprocessEvent foliaPlayerCommandPreprocessEvent = new FoliaPlayerCommandPreprocessEvent(getFoliaPlayer(event.getPlayer()), command);
        sendWebsocketMessage(foliaPlayerCommandPreprocessEvent);
    }

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        if (!config.getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement();

        FoliaAdvancement foliaAdvancement = getFoliaAdvancement(advancement);

        FoliaPlayerAdvancementDoneEvent foliaPlayerAdvancementDoneEvent = new FoliaPlayerAdvancementDoneEvent(getFoliaPlayer(event.getPlayer()), foliaAdvancement);
        sendWebsocketMessage(foliaPlayerAdvancementDoneEvent);
    }


}