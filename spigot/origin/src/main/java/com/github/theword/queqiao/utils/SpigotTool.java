package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Display;
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
        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealth != null) playerModel.setMaxHealth(maxHealth.getBaseValue());
        playerModel.setExperienceLevel(player.getLevel());
        playerModel.setExperienceProgress((double) player.getExp());
        playerModel.setTotalExperience(player.getTotalExperience());

        playerModel.setWalkSpeed((double) player.getWalkSpeed());
        playerModel.setX(player.getLocation().getX());
        playerModel.setY(player.getLocation().getY());
        playerModel.setZ(player.getLocation().getZ());
        return playerModel;
    }


    public static AchievementModel getSpigotAdvancementDisplay(AdvancementDisplay display) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();

        displayModel.setTitle(new TranslateModel(null, null, display.getTitle()));
        displayModel.setDescription(new TranslateModel(null, null, display.getDescription()));
        displayModel.setFrame(display.getType().toString());

        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }

    public static BaseComponent[] buildComponent(JsonElement jsonElement) {
        return ComponentSerializer.parse(jsonElement.toString());
    }
}