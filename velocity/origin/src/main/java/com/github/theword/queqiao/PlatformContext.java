package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.VelocityTool;
import com.google.gson.JsonElement;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.theword.queqiao.core.constant.ServerTypeConstant;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.platform.AbstractPlatformContext;
import io.github.theword.queqiao.core.platform.PlatformResult;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.Collection;
import java.util.UUID;

/**
 * Velocity 平台上下文：core 与 Velocity 代理之间的唯一接入点。
 */
public class PlatformContext extends AbstractPlatformContext<ProxyServer, Component, Player> {

    /**
     * 由 {@link QueQiao} 在 Runtime 创建后回填，用于读取消息前缀。
     */
    private volatile QueQiaoRuntime runtime;

    public PlatformContext(ProxyServer server) {
        super(server);
    }

    public void bindRuntime(QueQiaoRuntime runtime) {
        this.runtime = runtime;
    }

    public QueQiaoRuntime getRuntime() {
        return runtime;
    }

    @Override
    public String getServerType() {
        return ServerTypeConstant.VELOCITY;
    }

    @Override
    public String getServerVersion() {
        return server.getVersion().getVersion();
    }

    @Override
    public Component jsonToComponent(JsonElement json) {
        return GsonComponentSerializer.gson().deserializeFromTree(json);
    }

    @Override
    public Collection<Player> getPlayers() {
        return server.getAllPlayers();
    }

    @Override
    public String getPlayerName(Player player) {
        return player.getUsername();
    }

    @Override
    public UUID getPlayerUUID(Player player) {
        return player.getUniqueId();
    }

    @Override
    public PlayerModel getPlayerModel(Player player) {
        return VelocityTool.getVelocityPlayer(player);
    }

    @Override
    public PlatformResult<Void> broadcast(Component component) {
        server.sendMessage(withPrefix(component));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(Player player, Component component) {
        player.sendMessage(withPrefix(component));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(Component title, Component subtitle,
                                                   int fadeIn, int stay, int fadeOut) {
        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn),
                Duration.ofMillis(stay),
                Duration.ofMillis(fadeOut)
        );
        server.showTitle(Title.title(
                title == null ? Component.empty() : title,
                subtitle == null ? Component.empty() : subtitle,
                times
        ));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(Component component) {
        server.sendActionBar(component);
        return PlatformResult.success(null);
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private Component withPrefix(Component component) {
        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        Component result = prefix == null ? Component.empty() : jsonToComponent(prefix);
        return result.append(component);
    }
}
