package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerAchievementEvent;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
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

import static com.github.theword.queqiao.utils.FabricTool.getFabricAchievement;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(PlayerAdvancementTracker.class)
public abstract class AdvancementMixin {

    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At("TAIL"))
    // IF < fabric-1.20.4
//    private void onGrantCriterion(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
//        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
//        AchievementModel achievementModel = getFabricAchievement(advancement);
    // ELSE
//    private void onGrantCriterion(AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir) {
//        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
//        AchievementModel achievementModel = getFabricAchievement(advancementEntry.value());
        // END IF
        PlayerAchievementEvent event = new PlayerAchievementEvent(getFabricPlayer(owner), achievementModel);
        GlobalContext.sendEvent(event);
    }
}