package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.event.fabric.FabricAdvancementCriterionEvent;
import com.github.theword.queqiao.event.fabric.dto.advancement.FabricAdvancement;
// IF < fabric-1.20.4
//import com.github.theword.queqiao.tool.GlobalContext;
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

import static com.github.theword.queqiao.utils.FabricTool.getFabricAdvancement;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(PlayerAdvancementTracker.class)
public abstract class AdvancementMixin {

    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At("TAIL"))
    // IF < fabric-1.20.4
//    private void onGrantCriterion(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
//        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
//        FabricAdvancement fabricAdvancement = getFabricAdvancement(advancement);
    // ELSE
//    private void onGrantCriterion(AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir) {
//        if (!config.getSubscribeEvent().isPlayerAdvancement()) return;
//        FabricAdvancement fabricAdvancement = getFabricAdvancement(advancementEntry.value());
//        fabricAdvancement.setId(advancementEntry.id().toString());
        // END IF
        FabricAdvancementCriterionEvent fabricAdvancementCriterionEvent = new FabricAdvancementCriterionEvent(getFabricPlayer(owner), fabricAdvancement);
        GlobalContext.getWebsocketManager().sendEvent(fabricAdvancementCriterionEvent);
    }
}