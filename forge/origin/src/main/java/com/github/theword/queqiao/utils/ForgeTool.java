package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;

import net.minecraft.advancements.DisplayInfo;
// IF > forge-1.16.5
//import net.minecraft.server.level.ServerPlayer;
// ELSE
//import net.minecraft.util.text.TranslationTextComponent;
//import net.minecraft.entity.player.ServerPlayerEntity;
// END IF

// IF forge-1.18.2
//import net.minecraft.network.chat.TranslatableComponent;
// ELSE IF >= 1.19
import net.minecraft.network.chat.contents.TranslatableContents;
// END IF

public class ForgeTool {
    // IF > forge-1.16.5
//    public static PlayerModel getForgePlayer(ServerPlayer forgeServerPlayer) {
        // ELSE
//    public static PlayerModel getForgePlayer(ServerPlayerEntity forgeServerPlayer) {
        // END IF
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(forgeServerPlayer.getName().getString());
        playerModel.setUuid(forgeServerPlayer.getUUID());
        playerModel.setAddress(forgeServerPlayer.getIpAddress());
        playerModel.setHealth((double) forgeServerPlayer.getHealth());
        playerModel.setMaxHealth((double) forgeServerPlayer.getMaxHealth());
        playerModel.setExperienceLevel(forgeServerPlayer.experienceLevel);
        playerModel.setExperienceProgress((double) forgeServerPlayer.experienceProgress);
        playerModel.setTotalExperience(forgeServerPlayer.totalExperience);
        playerModel.setOp(forgeServerPlayer.hasPermissions(2));
        playerModel.setWalkSpeed((double) forgeServerPlayer.getSpeed());
        playerModel.setX(forgeServerPlayer.getX());
        playerModel.setY(forgeServerPlayer.getY());
        playerModel.setZ(forgeServerPlayer.getZ());
        return playerModel;
    }

    public static AchievementModel getForgeAchievement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        // IF >= forge-1.21
//        if (advancement.display().isEmpty()) {
//            return achievementModel;
//        }
//        if (advancement.parent().isPresent()) {
//            achievementModel.setKey(advancement.parent().get().toString());
//        }
//        DisplayInfo displayInfo = advancement.display().get();
        // ELSE
//        achievementModel.setKey(advancement.getId().toString());
//        if (advancement.getDisplay() == null) {
//            return achievementModel;
//        }
//        DisplayInfo displayInfo = advancement.getDisplay();
        // END IF

        // IF >= forge-1.21
//        displayModel.setFrame(displayInfo.getType().toString());
        // ELSE
//        displayModel.setFrame(displayInfo.getFrame().toString());
        // END IF

        // IF forge-1.16.5
//        displayModel.setTitle(((TranslationTextComponent) displayInfo.getTitle()).getKey());
//        displayModel.setDescription(((TranslationTextComponent) displayInfo.getDescription()).getKey());
        // ELSE IF forge-1.18.2
//        displayModel.setTitle(((TranslatableComponent) displayInfo.getTitle()).getKey());
//        displayModel.setDescription(((TranslatableComponent) displayInfo.getDescription()).getKey());
        // ELSE
//        displayModel.setTitle(((TranslatableContents) displayInfo.getTitle().getContents()).getKey());
//        displayModel.setDescription(((TranslatableContents) displayInfo.getDescription().getContents()).getKey());
        // END IF

        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }
}