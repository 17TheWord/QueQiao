package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;


public class FoliaTool {

    public static String getComponentText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getComponentJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }


    /**
     * 获取SpigotPlayer
     *
     * @param foliaPlayer 玩家
     * @return FoliaPlayer
     */
    public static PlayerModel getFoliaPlayer(Player foliaPlayer) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(foliaPlayer.getName());
        playerModel.setUuid(foliaPlayer.getUniqueId());

        if (foliaPlayer.getAddress() != null)
            playerModel.setAddress(foliaPlayer.getAddress().getHostString());

        playerModel.setHealth(foliaPlayer.getHealth());
//        playerModel.setMaxHealth(foliaPlayer.getMaxHealth()); // Deprecated
        playerModel.setExperienceLevel(foliaPlayer.getLevel());
        playerModel.setExperienceProgress((double) foliaPlayer.getExp());
        playerModel.setTotalExperience(foliaPlayer.getTotalExperience());

        playerModel.setOp(foliaPlayer.isOp());
        playerModel.setWalkSpeed((double) foliaPlayer.getWalkSpeed());
        playerModel.setX(foliaPlayer.getLocation().getX());
        playerModel.setY(foliaPlayer.getLocation().getY());
        playerModel.setZ(foliaPlayer.getLocation().getZ());

        return playerModel;
    }
}