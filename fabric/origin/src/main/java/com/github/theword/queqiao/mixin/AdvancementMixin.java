package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.callback.PlayerAdvancementCallback;
import net.minecraft.advancement.AdvancementProgress;
// IF < fabric-1.20.4
//import net.minecraft.advancement.Advancement;
// ELSE
//import net.minecraft.advancement.AdvancementEntry;
// END IF
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class AdvancementMixin {

    @Shadow
    private ServerPlayerEntity owner;

    // IF < fabric-1.20.4
//    @Shadow
//    public abstract AdvancementProgress getProgress(Advancement par1);
//
//    @Inject(
//            method = "grantCriterion",
//            at = @At("RETURN")
//    )
//    private void onGrantCriterion(
//            Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (!this.getProgress(advancement).isDone()) return;
//
//        PlayerAdvancementCallback.EVENT.invoker().onAdvancement(owner, advancement);
//    }
    // ELSE
//    @Shadow
//    public abstract AdvancementProgress getProgress(AdvancementEntry advancement);
//
//    @Inject(
//            method = "grantCriterion",
//            at = @At("RETURN")
//    )
//    private void onGrantCriterion(
//            AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (!this.getProgress(advancementEntry).isDone()) return;
//
//        PlayerAdvancementCallback.EVENT.invoker().onAdvancement(owner, advancementEntry);
//    }
    // END IF
}
