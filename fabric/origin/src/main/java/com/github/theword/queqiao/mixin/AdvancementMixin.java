package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.callback.PlayerAdvancementCallback;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class AdvancementMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At("RETURN"))
    private void onGrantCriterion(AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        AdvancementProgress progress = ((PlayerAdvancements) (Object) this).getOrStartProgress(holder);
        if (!progress.isDone()) return;

        PlayerAdvancementCallback.EVENT.invoker().onAdvancement(player, holder);
    }
}