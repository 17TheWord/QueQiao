package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.ForgeTool;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.constant.ServerTypeConstant;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.platform.AbstractPlatformContext;
import io.github.theword.queqiao.core.platform.PlatformResult;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collection;
import java.util.UUID;

/**
 * Forge 平台上下文：core 与 Forge 服务端之间的唯一接入点。
 *
 * <p>线程契约：core 可能从 WebSocket 工作线程直接调用这里的方法，
 * 本实现只做发包这类线程安全的操作。
 */
public class PlatformContext extends AbstractPlatformContext<MinecraftServer, ITextComponent, EntityPlayerMP> {

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
        return ServerTypeConstant.FORGE;
    }

    @Override
    public String getServerVersion() {
        return server.getMinecraftVersion();
    }

    @Override
    public ITextComponent jsonToComponent(JsonElement json) {
        return ForgeTool.buildComponent(json);
    }

    @Override
    public Collection<EntityPlayerMP> getPlayers() {
        return server.getPlayerList().getPlayers();
    }

    @Override
    public String getPlayerName(EntityPlayerMP player) {
        return player.getName();
    }

    @Override
    public UUID getPlayerUUID(EntityPlayerMP player) {
        return player.getUniqueID();
    }

    @Override
    public PlayerModel getPlayerModel(EntityPlayerMP player) {
        return ForgeTool.getForgePlayer(player);
    }

    @Override
    public PlatformResult<Void> broadcast(ITextComponent component) {
        sendPacket(new SPacketChat(withPrefix(component), ChatType.SYSTEM));
        return PlatformResult.<Void>success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(EntityPlayerMP player, ITextComponent component) {
        player.sendMessage(withPrefix(component));
        return PlatformResult.<Void>success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(ITextComponent title, ITextComponent subtitle, int fadeIn, int stay, int fadeOut) {
        if (title != null) {
            sendPacket(new SPacketTitle(SPacketTitle.Type.TITLE, title, fadeIn, stay, fadeOut));
        }
        if (subtitle != null) {
            sendPacket(new SPacketTitle(SPacketTitle.Type.SUBTITLE, subtitle, fadeIn, stay, fadeOut));
        }
        return PlatformResult.<Void>success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(ITextComponent component) {
        sendPacket(new SPacketTitle(SPacketTitle.Type.ACTIONBAR, component));
        return PlatformResult.<Void>success(null);
    }

    /**
     * 把 MC 的翻译组件拆成 {@link TranslateModel}，翻译文本由平台翻译服务提供。
     */
    @Override
    public TranslateModel parseTranslateModel(ITextComponent component) {
        if (!(component instanceof TextComponentTranslation)) {
            return new TranslateModel(null, null, component.getUnformattedText());
        }

        TextComponentTranslation translation = (TextComponentTranslation) component;
        return buildTranslateModel(
                translation.getKey(),
                translation.getFormatArgs(),
                arg -> arg instanceof ITextComponent
                        ? parseTranslateModel((ITextComponent) arg)
                        : new TranslateModel(null, null, String.valueOf(arg)),
                component.getUnformattedText()
        );
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private ITextComponent withPrefix(ITextComponent component) {
        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        if (prefix == null) {
            return component;
        }
        return ForgeTool.buildComponent(prefix).appendSibling(component);
    }

    private void sendPacket(Packet<?> packet) {
        for (EntityPlayerMP player : getPlayers()) {
            player.connection.sendPacket(packet);
        }
    }
}
