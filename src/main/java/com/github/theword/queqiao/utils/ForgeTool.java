package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.minecraft.entity.player.EntityPlayerMP;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ForgeTool {

    public static PlayerModel getPlayerModel(EntityPlayerMP forgePlayer) {
        PlayerModel player = new PlayerModel();
        player.setNickname(forgePlayer.getDisplayName());
        player.setUuid(forgePlayer.getUniqueID());
        player.setAddress(forgePlayer.getPlayerIP());
        player.setHealth((double) forgePlayer.getHealth());
        player.setMaxHealth((double) forgePlayer.getMaxHealth());
        player.setExperienceLevel(player.getExperienceLevel());
        player.setExperienceProgress((double) forgePlayer.experience);
        player.setTotalExperience(forgePlayer.experienceTotal);
        player.setOp(forgePlayer.mcServer.getConfigurationManager().func_152607_e(forgePlayer.getGameProfile()));
        player.setWalkSpeed((double) forgePlayer.capabilities.getWalkSpeed());
        player.setX(forgePlayer.posX);
        player.setY(forgePlayer.posY);
        player.setZ(forgePlayer.posZ);
        return player;
    }

    public static AchievementModel getForgeAchievement(Achievement achievement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        achievementModel.setKey(achievement.statId);
        String frameType = achievement.getSpecial() ? "goal" : "task";
        displayModel.setFrame(frameType);
        displayModel.setTitle(((ChatComponentTranslation) achievement.func_150951_e()).getKey());
//        displayModel.setDescription(achievement.getDescription());  // Client Only
        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }

    public static IChatComponent buildComponent(JsonElement jsonElement) {
        return IChatComponent.Serializer.func_150699_a(jsonElement.toString());
    }

}
