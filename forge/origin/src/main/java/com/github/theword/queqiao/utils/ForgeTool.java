package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;

import net.minecraft.advancements.DisplayInfo;
// IF > forge-1.16.5
//import net.minecraft.server.level.ServerPlayer;
// ELSE
//import net.minecraft.entity.player.ServerPlayerEntity;
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
//        DisplayInfo displayInfo = advancement.display().get();
        // ELSE
//        if (advancement.getDisplay() == null) {
//            return achievementModel;
//        }
//        DisplayInfo displayInfo = advancement.getDisplay();
        // END IF
        displayModel.setAnnounceChat(displayInfo.shouldAnnounceChat());
        displayModel.setDescription(displayInfo.getDescription().getString());

        // IF >= forge-1.21
//        if (displayInfo.getBackground().isPresent())
//            displayModel.setBackground(displayInfo.getBackground().toString());
//        displayModel.setFrame(displayInfo.getType().toString());
        // ELSE
//        displayModel.setFrame(displayInfo.getFrame().toString());
//        if (displayInfo.getBackground() != null)
//            displayModel.setBackground(displayInfo.getBackground().toString());
        // END IF
        displayModel.setHidden(displayInfo.isHidden());
        displayModel.setIcon(displayInfo.getIcon().toString());
        displayModel.setShowToast(displayInfo.shouldShowToast());
        displayModel.setTitle(displayInfo.getTitle().getString());
        displayModel.setX((double) displayInfo.getX());
        displayModel.setY((double) displayInfo.getY());
        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }
}