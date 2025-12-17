package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.Objects;


public class PaperTool {

    /**
     * 获取PaperPlayer
     *
     * @param player 玩家
     * @return PlayerModel
     */
    public static PlayerModel getPaperPlayer(Player player) {
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


    public static AchievementModel getPaperAdvancement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(advancement.getKey().toString());
        DisplayModel displayModel = new DisplayModel();

        AdvancementDisplay advancementDisplay = advancement.getDisplay();
        if (advancementDisplay == null) {
            achievementModel.setDisplay(displayModel);
            return achievementModel;
        }

        displayModel.setTitle(((TranslatableComponent) advancementDisplay.title()).key());
        displayModel.setDescription(((TranslatableComponent) advancementDisplay.description()).key());
        displayModel.setFrame(advancementDisplay.frame().name());

        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }

    public static Component buildComponent(JsonElement jsonElement) {
        return GsonComponentSerializer.gson().deserializeFromTree(jsonElement);
    }

    public static String getComponentText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getComponentJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }
}