package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.event.fabric.FabricServerLivingEntityAfterDeathEvent;
import com.github.theword.queqiao.event.fabric.FabricServerPlayConnectionDisconnectEvent;
import com.github.theword.queqiao.tool.GlobalContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        String message = player.getDamageTracker().getDeathMessage().getString();
        FabricServerLivingEntityAfterDeathEvent event = new FabricServerLivingEntityAfterDeathEvent("", getFabricPlayer(player), message);
        GlobalContext.getWebsocketManager().sendEvent(event);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnected(CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        FabricServerPlayConnectionDisconnectEvent event = new FabricServerPlayConnectionDisconnectEvent(getFabricPlayer(player));
        GlobalContext.getWebsocketManager().sendEvent(event);
    }

}