package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.event.fabric.FabricServerLivingEntityAfterDeathEvent;
import com.github.theword.queqiao.tool.event.fabric.FabricServerPlayConnectionDisconnectEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;
import static com.github.theword.queqiao.tool.utils.Tool.config;
import static com.github.theword.queqiao.tool.utils.Tool.sendWebsocketMessage;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        if (!config.getSubscribe_event().isPlayer_death()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        String message = player.getDamageTracker().getDeathMessage().getString();
        FabricServerLivingEntityAfterDeathEvent event = new FabricServerLivingEntityAfterDeathEvent("", getFabricPlayer(player), message);
        sendWebsocketMessage(event);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnected(CallbackInfo ci) {
        if (!config.getSubscribe_event().isPlayer_quit()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        String message = player.getDisplayName().getString();
        FabricServerPlayConnectionDisconnectEvent event = new FabricServerPlayConnectionDisconnectEvent(getFabricPlayer(player));
        sendWebsocketMessage(event);
    }

}