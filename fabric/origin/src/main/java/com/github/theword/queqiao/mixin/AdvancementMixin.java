package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerAchievementEvent;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.github.theword.queqiao.utils.FabricTool;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.github.theword.queqiao.utils.FabricTool.getFabricAchievementDisplay;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(PlayerAdvancements.class)
public abstract class AdvancementMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(
            method = "award",
            at = @At("RETURN"))
    private void onGrantCriterion(
            AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir
    ) {
        if (!cir.getReturnValue()) return;

        AdvancementProgress progress = ((PlayerAdvancements) (Object) this).getOrStartProgress(holder);
        if (!progress.isDone()) return;

        Advancement value = holder.value();
        Optional<DisplayInfo> displayInfoOptional = value.display();
        if (displayInfoOptional.isEmpty()) return;

        DisplayInfo display = displayInfoOptional.get();
        if (!display.shouldAnnounceChat()) return;
//        if (!player.level().getGameRules().get(GameRules.SHOW_ADVANCEMENT_MESSAGES)) return;
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;

        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(holder.id().toString());
        DisplayModel displayModel = getFabricAchievementDisplay(display);
        achievementModel.setDisplay(displayModel);
        achievementModel.setTranslation(FabricTool.parseTranslateModel(display.getTitle()));
        PlayerAchievementEvent event = new PlayerAchievementEvent(getFabricPlayer(player), achievementModel);
        GlobalContext.sendEvent(event);
    }
}