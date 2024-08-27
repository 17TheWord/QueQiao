package com.github.theword.queqiao;

import com.github.theword.queqiao.event.velocity.*;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import static com.github.theword.queqiao.tool.utils.Tool.config;
import static com.github.theword.queqiao.tool.utils.Tool.sendWebsocketMessage;
import static com.github.theword.queqiao.utils.VelocityTool.getVelocityPlayer;


public class EventProcessor {

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        if (!config.getSubscribeEvent().isPlayerChat()) return;

        VelocityPlayer player = getVelocityPlayer(event.getPlayer());
        String message = event.getMessage();
        VelocityPlayerChatEvent velocityPlayerChatEvent = new VelocityPlayerChatEvent(player, message);
        sendWebsocketMessage(velocityPlayerChatEvent);
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        if (!config.getSubscribeEvent().isPlayerJoin()) return;

        VelocityPlayer player = getVelocityPlayer(event.getPlayer());
        VelocityLoginEvent velocityLoginEvent = new VelocityLoginEvent(player);
        sendWebsocketMessage(velocityLoginEvent);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (!config.getSubscribeEvent().isPlayerQuit()) return;

        VelocityPlayer player = getVelocityPlayer(event.getPlayer());
        VelocityDisconnectEvent velocityDisconnectEvent = new VelocityDisconnectEvent(player);
        sendWebsocketMessage(velocityDisconnectEvent);
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player player) || !config.getSubscribeEvent().isPlayerCommand()) return;

        String command = event.getCommand();

        VelocityCommandExecuteEvent velocityCommandExecuteEvent = new VelocityCommandExecuteEvent(getVelocityPlayer(player), command);
        sendWebsocketMessage(velocityCommandExecuteEvent);
    }
}