package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;


public class FoliaTool {

    public static Component buildComponent(JsonElement jsonElement) {
        return GsonComponentSerializer.gson().deserializeFromTree(jsonElement);
    }

    public static String getComponentText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getComponentJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    /**
     * 获取 FoliaPlayer
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

    public static AchievementModel getFoliaAdvancement(PlatformContext context, String nickname, Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(advancement.getKey().toString());
        DisplayModel displayModel = new DisplayModel();

        AdvancementDisplay advancementDisplay = advancement.getDisplay();
        if (advancementDisplay == null) {
            achievementModel.setDisplay(displayModel);
            return achievementModel;
        }

        displayModel.setTitle(context.parseTranslateModel(advancementDisplay.title()));
        displayModel.setDescription(context.parseTranslateModel(advancementDisplay.description()));
        displayModel.setFrame(advancementDisplay.frame().name());
        achievementModel.setDisplay(displayModel);

        Component translatable = Component.translatable(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                Component.text(nickname),
                advancement.displayName()
        );
        achievementModel.setTranslation(context.parseTranslateModel(translatable));
        return achievementModel;
    }
}
