package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.advancement.Advancement;
// IF >= spigot-1.13
//import org.bukkit.advancement.AdvancementDisplay;
// END IF
import org.bukkit.entity.Player;


public class SpigotTool {
    /**
     * 获取SpigotPlayer
     *
     * @param player 玩家
     * @return SpigotPlayer
     */
    public static PlayerModel getSpigotPlayer(Player player) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(player.getName());
        playerModel.setUuid(player.getUniqueId());
        playerModel.setOp(player.isOp());
        playerModel.setHealth(player.getHealth());
//        playerModel.setMaxHealth(player.getMaxHealth()); // Deprecated
        playerModel.setExperienceLevel(player.getLevel());
        playerModel.setExperienceProgress((double) player.getExp());
        playerModel.setTotalExperience(player.getTotalExperience());

        playerModel.setWalkSpeed((double) player.getWalkSpeed());
        playerModel.setX(player.getLocation().getX());
        playerModel.setY(player.getLocation().getY());
        playerModel.setZ(player.getLocation().getZ());
        return playerModel;
    }


    public static AchievementModel getSpigotAdvancement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
// IF >= spigot-1.13
//        if (advancement.getDisplay() == null) {
//            return achievementModel;
//        }
//        AdvancementDisplay advancementDisplay = advancement.getDisplay();
//
//        displayModel.setAnnounceChat(advancementDisplay.shouldAnnounceChat());
//        displayModel.setDescription(advancementDisplay.getDescription());
//        displayModel.setFrame(advancementDisplay.getType().toString());
//        displayModel.setHidden(advancementDisplay.isHidden());
//        displayModel.setIcon(advancementDisplay.getIcon().toString());
//        displayModel.setShowToast(advancementDisplay.shouldShowToast());
//        displayModel.setTitle(advancementDisplay.getTitle());
//        displayModel.setX((double) advancementDisplay.getX());
//        displayModel.setY((double) advancementDisplay.getY());
        // END IF
        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }

    public static BaseComponent[] buildComponent(JsonElement jsonElement) {
        return ComponentSerializer.parse(jsonElement.toString());
    }
}