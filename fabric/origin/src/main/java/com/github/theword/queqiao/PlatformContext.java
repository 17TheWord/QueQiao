package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.FabricTool;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.constant.ServerTypeConstant;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.platform.AbstractPlatformContext;
import io.github.theword.queqiao.core.platform.PlatformResult;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.UUID;

/**
 * Fabric 平台上下文：core 与 Fabric 服务端之间的唯一接入点。
 *
 * <p>线程契约：core 可能从 WebSocket 工作线程直接调用这里的方法，
 * 本实现只做"发包"这类线程安全的操作，因此不做主线程调度。
 */
public class PlatformContext extends AbstractPlatformContext<MinecraftServer, Component, ServerPlayer> {

    /**
     * 由 {@link QueQiao} 在 Runtime 创建后回填，用于读取消息前缀。
     */
    private volatile QueQiaoRuntime runtime;

    public PlatformContext(MinecraftServer server) {
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
        return ServerTypeConstant.FABRIC;
    }

    @Override
    public String getServerVersion() {
        return server.getServerVersion();
    }

    @Override
    public Component jsonToComponent(JsonElement json) {
        return FabricTool.buildComponent(json);
    }

    @Override
    public Collection<ServerPlayer> getPlayers() {
        return server.getPlayerList().getPlayers();
    }

    @Override
    public String getPlayerName(ServerPlayer player) {
        return player.getName().getString();
    }

    @Override
    public UUID getPlayerUUID(ServerPlayer player) {
        return player.getUUID();
    }

    @Override
    public PlayerModel getPlayerModel(ServerPlayer player) {
        return FabricTool.getFabricPlayer(player);
    }

    @Override
    public PlatformResult<Void> broadcast(Component component) {
        sendPacket(new ClientboundSystemChatPacket(withPrefix(component), false));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(ServerPlayer player, Component component) {
        player.sendSystemMessage(withPrefix(component), false);
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        sendPacket(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
        if (title != null) {
            sendPacket(new ClientboundSetTitleTextPacket(title));
        }
        if (subtitle != null) {
            sendPacket(new ClientboundSetSubtitleTextPacket(subtitle));
        }
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(Component component) {
        sendPacket(new ClientboundSetActionBarTextPacket(component));
        return PlatformResult.success(null);
    }

    /**
     * 把 MC 的翻译组件拆成 {@link TranslateModel}，翻译文本由平台翻译服务提供。
     */
    @Override
    public TranslateModel parseTranslateModel(Component component) {
        if (!(component.getContents() instanceof TranslatableContents translatableContents)) {
            return new TranslateModel(null, null, component.getString());
        }

        return buildTranslateModel(
                translatableContents.getKey(),
                translatableContents.getArgs(),
                arg -> arg instanceof Component child
                        ? parseTranslateModel(child)
                        : new TranslateModel(null, null, String.valueOf(arg)),
                component.getString()
        );
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private MutableComponent withPrefix(Component component) {
        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        MutableComponent result = prefix == null ? Component.empty() : FabricTool.buildComponent(prefix);
        return result.append(component);
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(packet);
        }
    }
}