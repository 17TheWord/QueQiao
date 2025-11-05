package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.level.ServerPlayer;

public class NeoForgeTool {
    public static PlayerModel getNeoForgePlayer(ServerPlayer neoForgePlayer) {
        PlayerModel player = new PlayerModel();
        player.setNickname(neoForgePlayer.getName().getString());
        player.setUuid(neoForgePlayer.getUUID());
        player.setAddress(neoForgePlayer.getIpAddress());
        player.setHealth((double) neoForgePlayer.getHealth());
        player.setMaxHealth((double) neoForgePlayer.getMaxHealth());
        player.setExperienceLevel(neoForgePlayer.experienceLevel);
        player.setExperienceProgress((double) neoForgePlayer.experienceProgress);
        player.setTotalExperience(neoForgePlayer.totalExperience);
        player.setOp(neoForgePlayer.hasPermissions(2));
        player.setWalkSpeed((double) neoForgePlayer.getAbilities().getWalkingSpeed());
        player.setX(neoForgePlayer.getX());
        player.setY(neoForgePlayer.getY());
        player.setZ(neoForgePlayer.getZ());
        return player;
    }

    public static AchievementModel getNeoForgeAchievement(Advancement advancement) {
        AchievementModel achievement = new AchievementModel();
        if (advancement.display().isEmpty()) {
            return achievement;
        }
        DisplayInfo displayInfo = advancement.display().get();

        DisplayModel display = new DisplayModel();
        display.setAnnounceChat(displayInfo.shouldAnnounceChat());

        if (displayInfo.getBackground().isPresent())
            display.setBackground(displayInfo.getBackground().toString());

        display.setDescription(displayInfo.getDescription().getString());
        display.setFrame(displayInfo.getType().toString());
        display.setHidden(displayInfo.isHidden());
        display.setIcon(displayInfo.getIcon().toString());
        display.setShowToast(displayInfo.shouldShowToast());
        display.setTitle(displayInfo.getTitle().getString());
        display.setX((double) displayInfo.getX());
        display.setY((double) displayInfo.getY());
        achievement.setDisplay(display);
        return achievement;
    }
}