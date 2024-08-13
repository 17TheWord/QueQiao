package com.github.theword.queqiao;

import com.github.theword.queqiao.event.spigot.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static com.github.theword.queqiao.tool.utils.Tool.config;
import static com.github.theword.queqiao.tool.utils.Tool.sendWebsocketMessage;


class EventProcessor implements Listener {
    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler
    void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || !config.getSubscribe_event().isPlayer_chat()) return;

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
        if (!config.getSubscribe_event().isPlayer_death()) return;

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
        if (!config.getSubscribe_event().isPlayer_join()) return;

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
        if (!config.getSubscribe_event().isPlayer_quit()) return;

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
        if (!config.getSubscribe_event().isPlayer_command()) return;

        String command = event.getMessage();

        if (command.startsWith("/l ") || command.startsWith("/login ") || command.startsWith("/register ") || command.startsWith("/reg ") || command.startsWith("mcqq "))
            return;

        command = command.replaceFirst("/", "");
        SpigotPlayerCommandPreprocessEvent spigotPlayerCommandPreprocessEvent = new SpigotPlayerCommandPreprocessEvent(getSpigotPlayer(event.getPlayer()), command);
        sendWebsocketMessage(spigotPlayerCommandPreprocessEvent);


    }

    /**
     * 获取SpigotPlayer
     *
     * @param player 玩家
     * @return SpigotPlayer
     */
    SpigotPlayer getSpigotPlayer(Player player) {
        SpigotPlayer spigotPlayer = new SpigotPlayer();
        spigotPlayer.setUuid(player.getUniqueId());
        spigotPlayer.setNickname(player.getName());
        spigotPlayer.setDisplayName(player.getDisplayName());
        spigotPlayer.setPlayerListName(player.getDisplayName());
        spigotPlayer.setAddress((Objects.requireNonNull(player.getAddress()).toString()));
        spigotPlayer.setHealthScale(player.getHealthScale());
        spigotPlayer.setExp(player.getExp());
        spigotPlayer.setTotalExp(player.getTotalExperience());
        spigotPlayer.setLevel(player.getLevel());
        spigotPlayer.setLocale(player.getLocale());
        // IF spigot-1.12.2
//        spigotPlayer.setPing(-1);
        // ELSE
//        spigotPlayer.setPing(player.getPing());
        // END IF
        spigotPlayer.setPlayerTime(player.getPlayerTime());
        spigotPlayer.setPlayerTimeRelative(player.isPlayerTimeRelative());
        spigotPlayer.setPlayerTimeOffset(player.getPlayerTimeOffset());
        spigotPlayer.setWalkSpeed(player.getWalkSpeed());
        spigotPlayer.setFlySpeed(player.getFlySpeed());
        spigotPlayer.setAllowFlight(player.getAllowFlight());
        spigotPlayer.setSprinting(player.isSprinting());
        spigotPlayer.setSneaking(player.isSneaking());
        spigotPlayer.setFlying(player.isFlying());
        spigotPlayer.setOp(player.isOp());
        return spigotPlayer;
    }
}