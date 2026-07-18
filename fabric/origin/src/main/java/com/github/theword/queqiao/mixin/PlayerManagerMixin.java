package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.callback.PlayerCommandCallback;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class PlayerManagerMixin {

    @Shadow
    public abstract ServerPlayer getPlayer();

    @Inject(method = "handleChatCommand", at = @At("HEAD"))
    private void onPlayerUnsignedCommand(ServerboundChatCommandPacket packet, CallbackInfo ci) {
        PlayerCommandCallback.EVENT.invoker().onCommand(getPlayer(), packet.command());
    }

    @Inject(method = "handleSignedChatCommand", at = @At("HEAD"))
    private void onPlayerSignedCommand(ServerboundChatCommandSignedPacket packet, CallbackInfo ci) {
        PlayerCommandCallback.EVENT.invoker().onCommand(getPlayer(), packet.command());
    }
}