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
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * GTNH 1.7.10 平台上下文：core 与 1.7.10 Forge 服务端之间的唯一接入点。
 *
 * <p>线程契约：core 可能从 WebSocket 工作线程直接调用这里的方法，
 * 本实现只做发包这类线程安全的操作。
 */
public class PlatformContext extends AbstractPlatformContext<MinecraftServer, IChatComponent, EntityPlayerMP> {

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
    public IChatComponent jsonToComponent(JsonElement json) {
        return ForgeTool.buildComponent(json);
    }

    @Override
    public Collection<EntityPlayerMP> getPlayers() {
        return new ArrayList<EntityPlayerMP>(server.getConfigurationManager().playerEntityList);
    }

    @Override
    public String getPlayerName(EntityPlayerMP player) {
        return player.getDisplayName();
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
    public PlatformResult<Void> broadcast(IChatComponent component) {
        sendPacket(new S02PacketChat(withPrefix(component), true));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(EntityPlayerMP player, IChatComponent component) {
        player.addChatComponentMessage(withPrefix(component));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(IChatComponent title, IChatComponent subtitle, int fadeIn, int stay, int fadeOut) {
        if (title != null) {
            sendPacket(new S02PacketChat(title, false));
        }
        if (subtitle != null) {
            sendPacket(new S02PacketChat(subtitle, false));
        }
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(IChatComponent component) {
        sendPacket(new S02PacketChat(component, false));
        return PlatformResult.success(null);
    }

    /**
     * 把 MC 的翻译组件拆成 {@link TranslateModel}，翻译文本由平台翻译服务提供。
     */
    @Override
    public TranslateModel parseTranslateModel(IChatComponent component) {
        if (!(component instanceof ChatComponentTranslation)) {
            return new TranslateModel(null, null, component.getUnformattedText());
        }

        ChatComponentTranslation translation = (ChatComponentTranslation) component;
        return buildTranslateModel(
                translation.getKey(),
                translation.getFormatArgs(),
                arg -> arg instanceof IChatComponent
                        ? parseTranslateModel((IChatComponent) arg)
                        : new TranslateModel(null, null, String.valueOf(arg)),
                component.getUnformattedText()
        );
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private IChatComponent withPrefix(IChatComponent component) {
        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        if (prefix == null) {
            return component;
        }
        return ForgeTool.buildComponent(prefix).appendSibling(component);
    }

    private void sendPacket(Packet packet) {
        for (EntityPlayerMP player : getPlayers()) {
            player.playerNetServerHandler.sendPacket(packet);
        }
    }
}
