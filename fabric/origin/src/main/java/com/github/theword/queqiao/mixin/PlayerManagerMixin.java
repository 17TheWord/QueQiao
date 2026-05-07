package com.github.theword.queqiao.mixin;


import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerChatEvent;
import com.github.theword.queqiao.tool.event.PlayerCommandEvent;
import com.github.theword.queqiao.tool.event.PlayerQuitEvent;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class PlayerManagerMixin {

    @Shadow
    public abstract ServerPlayer getPlayer();

    @Inject(method = "onDisconnect", at = @At("RETURN"))
    private void onPlayerQuit(DisconnectionDetails details, CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;
        PlayerQuitEvent event = new PlayerQuitEvent(getFabricPlayer(getPlayer()));
        GlobalContext.sendEvent(event);
    }

    @Inject(method = "sendPlayerChatMessage", at = @At("RETURN"))
    private void onPlayerChat(PlayerChatMessage message, ChatType.Bound chatType, CallbackInfo ci) {
        Component component = message.decoratedContent();
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;
        PlayerChatEvent event = new PlayerChatEvent(getFabricPlayer(getPlayer()), "", component.getString(), component.getString());
        GlobalContext.sendEvent(event);
    }

    @Inject(method = "handleChatCommand", at = @At("HEAD"))
    private void onPlayerUnsignedCommand(ServerboundChatCommandPacket packet, CallbackInfo ci) {
        sendPlayerCommandEvent(packet.command());
    }

    @Inject(method = "handleSignedChatCommand", at = @At("HEAD"))
    private void onPlayerSignedCommand(ServerboundChatCommandSignedPacket packet, CallbackInfo ci) {
        sendPlayerCommandEvent(packet.command());
    }

    @Unique
    private void sendPlayerCommandEvent(String command) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;
        PlayerCommandEvent event = new PlayerCommandEvent(getFabricPlayer(getPlayer()), "", command, command);
        GlobalContext.sendEvent(event);
    }
}