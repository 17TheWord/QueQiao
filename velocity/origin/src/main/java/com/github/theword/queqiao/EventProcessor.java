package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerCommandEvent;
import com.github.theword.queqiao.tool.event.PlayerJoinEvent;
import com.github.theword.queqiao.tool.event.PlayerQuitEvent;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import static com.github.theword.queqiao.utils.VelocityTool.getVelocityPlayer;


public class EventProcessor {

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        PlayerModel player = getVelocityPlayer(event.getPlayer());
        String message = event.getMessage();
        com.github.theword.queqiao.tool.event.PlayerChatEvent velocityPlayerChatEvent = new com.github.theword.queqiao.tool.event.PlayerChatEvent(player, "", message, message);
        GlobalContext.sendEvent(velocityPlayerChatEvent);
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        PlayerModel player = getVelocityPlayer(event.getPlayer());
        PlayerJoinEvent velocityLoginEvent = new PlayerJoinEvent(player);
        GlobalContext.sendEvent(velocityLoginEvent);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        PlayerModel player = getVelocityPlayer(event.getPlayer());
        PlayerQuitEvent velocityDisconnectEvent = new PlayerQuitEvent(player);
        GlobalContext.sendEvent(velocityDisconnectEvent);
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player player) || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand())
            return;

        String command = event.getCommand();

        PlayerCommandEvent velocityCommandExecuteEvent = new PlayerCommandEvent(getVelocityPlayer(player), "", command, command);
        GlobalContext.sendEvent(velocityCommandExecuteEvent);
    }
}