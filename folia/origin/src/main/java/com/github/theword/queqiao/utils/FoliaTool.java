package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;


public class FoliaTool {

    public static String getComponentText(Component component) {
        return PlainTextComponentSerializer.plainText().serializeOr(component, "");
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


    public static AchievementModel getFoliaAchievement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();

        AdvancementDisplay advancementDisplay = advancement.getDisplay();
        if (advancementDisplay == null) {
            return achievementModel;
        }
        displayModel.setAnnounceChat(advancementDisplay.doesAnnounceToChat());
        if (advancementDisplay.backgroundPath() != null)
            displayModel.setBackground(advancementDisplay.backgroundPath().toString());
        displayModel.setDescription(getComponentText(advancementDisplay.description()));
        displayModel.setFrame(advancementDisplay.frame().toString());
        displayModel.setHidden(advancementDisplay.isHidden());
        displayModel.setIcon(advancementDisplay.icon().translationKey());
        displayModel.setShowToast(advancementDisplay.doesShowToast());
        displayModel.setTitle(getComponentText(advancementDisplay.title()));
        return achievementModel;
    }
}