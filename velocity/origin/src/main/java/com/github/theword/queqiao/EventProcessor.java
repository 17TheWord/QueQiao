package com.github.theword.queqiao;

import com.github.theword.queqiao.event.velocity.VelocityDisconnectEvent;
import com.github.theword.queqiao.event.velocity.VelocityLoginEvent;
import com.github.theword.queqiao.event.velocity.VelocityPlayer;
import com.github.theword.queqiao.event.velocity.VelocityPlayerChatEvent;
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
        if (!config.getSubscribe_event().isPlayer_chat()) return;

        VelocityPlayer player = getVelocityPlayer(event.getPlayer());
        String message = event.getMessage();
        VelocityPlayerChatEvent velocityPlayerChatEvent = new VelocityPlayerChatEvent(player, message);
        sendWebsocketMessage(velocityPlayerChatEvent);
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        if (!config.getSubscribe_event().isPlayer_join()) return;

        VelocityPlayer player = getVelocityPlayer(event.getPlayer());
        VelocityLoginEvent velocityLoginEvent = new VelocityLoginEvent(player);
        sendWebsocketMessage(velocityLoginEvent);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (!config.getSubscribe_event().isPlayer_quit()) return;

        VelocityPlayer player = getVelocityPlayer(event.getPlayer());
        VelocityDisconnectEvent velocityDisconnectEvent = new VelocityDisconnectEvent(player);
        sendWebsocketMessage(velocityDisconnectEvent);
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player) || !config.getSubscribe_event().isPlayer_command()) return;

        Player player = (Player) event.getCommandSource();
        String command = event.getCommand();

    }
}
