package com.github.theword.queqiao.utils;

import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;


public class SpigotTool {
    /**
     * 获取 Spigot 玩家快照
     */
    public static PlayerModel getSpigotPlayer(Player player) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(player.getName());
        playerModel.setUuid(player.getUniqueId());
        playerModel.setOp(player.isOp());
        playerModel.setHealth(player.getHealth());
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
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

    /**
     * 1.12.2 / 1.13 的 Bukkit API 不暴露 AdvancementDisplay，只能上报成就 key。
     */
    public static AchievementModel getSpigotAdvancement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(advancement.getKey().toString());
        achievementModel.setDisplay(new DisplayModel());
        return achievementModel;
    }

    public static BaseComponent[] buildComponent(JsonElement jsonElement) {
        return ComponentSerializer.parse(jsonElement.toString());
    }
}
