package com.github.theword.queqiao;

import io.github.theword.queqiao.core.config.ConfigKeys;
import io.github.theword.queqiao.core.event.PlayerCommandEvent;
import io.github.theword.queqiao.core.event.PlayerJoinEvent;
import io.github.theword.queqiao.core.event.PlayerQuitEvent;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.function.Supplier;

public class EventProcessor {

    /**
     * 平台上下文在插件初始化时才创建，事件注册紧随其后，因此用供应商延迟取值。
     */
    private final Supplier<PlatformContext> contextSupplier;

    public EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
        String message = event.getMessage();
        runtime.sendEvent(new io.github.theword.queqiao.core.event.PlayerChatEvent(player, "", message, message));
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
        runtime.sendEvent(new PlayerJoinEvent(player));
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
        runtime.sendEvent(new PlayerQuitEvent(player));
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;
        if (!(event.getCommandSource() instanceof Player player)) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

        String command = runtime.utils.isIgnoredCommand(event.getCommand());
        if (command.isEmpty()) return;

        runtime.sendEvent(new PlayerCommandEvent(context.getPlayerModel(player), "", command, command));
    }

    /**
     * @return 已就绪的平台上下文；尚未初始化时返回 null
     */
    private PlatformContext currentContext() {
        PlatformContext context = contextSupplier.get();
        if (context == null || context.getRuntime() == null) {
            return null;
        }
        return context;
    }
}
