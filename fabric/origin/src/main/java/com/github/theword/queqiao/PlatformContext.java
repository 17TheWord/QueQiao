package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.FabricTool;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.constant.ServerTypeConstant;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.platform.AbstractPlatformContext;
import io.github.theword.queqiao.core.platform.PlatformResult;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
// IF > fabric-1.16.5
//import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
//import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
// END IF
// IF <= fabric-1.18.2
//import net.minecraft.network.MessageType;
// END IF
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
// IF < fabric-1.19
//import net.minecraft.text.TranslatableText;
// ELSE
//import net.minecraft.text.TranslatableTextContent;
// END IF

// IF >= fabric-1.20
//import net.minecraft.network.packet.Packet;
// ELSE IF >= fabric-1.18
//import net.minecraft.network.Packet;
// ELSE
//import net.minecraft.network.Packet;
// END IF

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Fabric 平台上下文：core 与 Fabric 服务端之间的唯一接入点。
 */
public class PlatformContext extends AbstractPlatformContext<MinecraftServer, Text, ServerPlayerEntity> {

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
        return server.getVersion();
    }

    @Override
    public Text jsonToComponent(JsonElement json) {
        return FabricTool.buildComponent(json);
    }

    @Override
    public Collection<ServerPlayerEntity> getPlayers() {
        return new ArrayList<ServerPlayerEntity>(server.getPlayerManager().getPlayerList());
    }

    @Override
    public String getPlayerName(ServerPlayerEntity player) {
        return player.getName().getString();
    }

    @Override
    public UUID getPlayerUUID(ServerPlayerEntity player) {
        return player.getUuid();
    }

    @Override
    public PlayerModel getPlayerModel(ServerPlayerEntity player) {
        return FabricTool.getFabricPlayer(player);
    }

    @Override
    public PlatformResult<Void> broadcast(Text component) {
        Text message = withPrefix(component);
        // IF >= fabric-1.19.2
//        sendPacket(new GameMessageS2CPacket(message, false));
        // ELSE
//        sendPacket(new GameMessageS2CPacket(message, MessageType.SYSTEM, UUID.randomUUID()));
        // END IF
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(ServerPlayerEntity player, Text component) {
        player.sendMessage(withPrefix(component), false);
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(Text title, Text subtitle, int fadeIn, int stay, int fadeOut) {
        // IF fabric-1.16.5
//        if (title != null) {
//            sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, title, fadeIn, stay, fadeOut));
//        }
//        if (subtitle != null) {
//            sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, subtitle, fadeIn, stay, fadeOut));
//        }
        // ELSE
//        sendPacket(new TitleFadeS2CPacket(fadeIn, stay, fadeOut));
//        if (title != null) {
//            sendPacket(new TitleS2CPacket(title));
//        }
//        if (subtitle != null) {
//            sendPacket(new SubtitleS2CPacket(subtitle));
//        }
        // END IF
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(Text component) {
        // IF >= fabric-1.19.2
//        sendPacket(new GameMessageS2CPacket(component, true));
        // ELSE
//        sendPacket(new GameMessageS2CPacket(component, MessageType.GAME_INFO, UUID.randomUUID()));
        // END IF
        return PlatformResult.success(null);
    }

    /**
     * 把 MC 的翻译组件拆成 {@link TranslateModel}，翻译文本由平台翻译服务提供。
     */
    @Override
    public TranslateModel parseTranslateModel(Text component) {
        // IF < fabric-1.19
//        if (!(component instanceof TranslatableText)) {
//            return new TranslateModel(null, null, component.getString());
//        }
//        TranslatableText translatableTextContent = (TranslatableText) component;
        // ELSE
//        if (!(component.getContent() instanceof TranslatableTextContent translatableTextContent)) {
//            return new TranslateModel(null, null, component.getString());
//        }
        // END IF

        return buildTranslateModel(
                translatableTextContent.getKey(),
                translatableTextContent.getArgs(),
                arg -> arg instanceof Text
                        ? parseTranslateModel((Text) arg)
                        : new TranslateModel(null, null, String.valueOf(arg)),
                component.getString()
        );
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private Text withPrefix(Text component) {
        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        if (prefix == null) {
            return component;
        }
        return FabricTool.buildComponent(prefix).append(component);
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(packet);
        }
    }
}