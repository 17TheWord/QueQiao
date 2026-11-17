package com.github.theword.queqiao;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.theword.queqiao.core.config.ConfigKeys;
import io.github.theword.queqiao.core.event.PlayerChatEvent;
import io.github.theword.queqiao.core.event.PlayerCommandEvent;
import io.github.theword.queqiao.core.event.PlayerJoinEvent;
import io.github.theword.queqiao.core.event.PlayerQuitEvent;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;

import java.util.function.Supplier;


public class EventProcessor {

    /**
     * 平台上下文在代理初始化时创建，事件在此之前不会触发，但仍用供应商保持时序无关。
     */
    private final Supplier<PlatformContext> contextSupplier;

    public EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @Subscribe
    public void onPlayerChat(com.velocitypowered.api.event.player.PlayerChatEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
        String message = event.getMessage();

        runtime.sendEvent(new PlayerChatEvent(player, "", message, message));
    }

    @Subscribe
    public void onPlayerLogin(PostLoginEvent event) {
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

        QueQiaoRuntime runtime = context.getRuntime();
        if (!(event.getCommandSource() instanceof Player player)) return;
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

        String command = runtime.utils.isIgnoredCommand(event.getCommand());
        if (command.isEmpty()) return;

        runtime.sendEvent(new PlayerCommandEvent(context.getPlayerModel(player), "", command, command));
    }

    /**
     * @return 已就绪的平台上下文；代理尚未完成初始化时返回 null
     */
    private PlatformContext currentContext() {
        PlatformContext context = contextSupplier.get();
        if (context == null || context.getRuntime() == null) {
            return null;
        }
        return context;
    }
}
