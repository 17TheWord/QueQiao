package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerAchievementEvent;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
// IF < fabric-1.20.4
//import net.minecraft.advancement.Advancement;
//import org.spongepowered.asm.mixin.injection.Inject;
// ELSE
//import net.minecraft.advancement.AdvancementEntry;
//import org.spongepowered.asm.mixin.injection.Inject;
//import java.util.Optional;
// END IF

// IF < fabric-1.19
//import net.minecraft.text.TranslatableText;
// END IF

import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import static com.github.theword.queqiao.utils.FabricTool.getFabricAchievementDisplay;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(PlayerAdvancementTracker.class)
public abstract class AdvancementMixin {

    @Shadow
    private ServerPlayerEntity owner;

    // IF < fabric-1.20.4
//    @Inject(
//            method = "grantCriterion",
//            at = @At("HEAD")
//    )
//    private void onGrantCriterion(
//            Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
//        AdvancementDisplay advancementDisplay = advancement.getDisplay();
//        if (advancementDisplay == null || !advancementDisplay.shouldAnnounceToChat()) return;
//
//        AchievementModel achievementModel = new AchievementModel();
//        DisplayModel displayModel = getFabricAchievementDisplay(advancementDisplay);
//        achievementModel.setKey(advancement.getId().toString());
        // IF < fabric-1.19
//        Text translateText = new TranslatableText("chat.type.advancement." + advancement.getDisplay().getFrame().getId(), this.owner.getDisplayName(), advancement.toHoverableText());
        // ELSE
//        Text translateText = Text.translatable("chat.type.advancement." + advancement.getDisplay().getFrame().getId(), this.owner.getDisplayName(), advancement.toHoverableText());
        // END IF
        // ELSE
//    @Inject(
//            method = "grantCriterion",
//            at = @At("HEAD"))
//    private void onGrantCriterion(
//            AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
//        Optional<AdvancementDisplay> displayOptional = advancementEntry.value().display();
//        if (displayOptional.isEmpty() || !displayOptional.get().shouldAnnounceToChat())
//            return;
//        AdvancementDisplay advancementDisplay = displayOptional.get();
//        Text translateText = advancementDisplay.getFrame().getChatAnnouncementText(advancementEntry, owner);
//        AchievementModel achievementModel = new AchievementModel();
//        achievementModel.setKey(advancementEntry.id().toString());
//        DisplayModel displayModel = getFabricAchievementDisplay(advancementEntry.value().display().get());
        // END IF
        achievementModel.setDisplay(displayModel);
        achievementModel.setText(translateText.getString());
        PlayerAchievementEvent event = new PlayerAchievementEvent(getFabricPlayer(owner), achievementModel);
        GlobalContext.sendEvent(event);
    }
}