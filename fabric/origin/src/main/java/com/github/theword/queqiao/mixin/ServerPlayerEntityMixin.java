package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.callback.PlayerDeathCallback;
import com.github.theword.queqiao.callback.PlayerQuitCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        PlayerDeathCallback.EVENT.invoker().onDeath((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnected(CallbackInfo ci) {
        PlayerQuitCallback.EVENT.invoker().onQuit((ServerPlayerEntity) (Object) this);
    }
}
