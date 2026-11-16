package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ForgeTool {

    public static PlayerModel getForgePlayer(EntityPlayerMP forgePlayer) {
        PlayerModel player = new PlayerModel();
        player.setNickname(forgePlayer.getDisplayName());
        player.setUuid(forgePlayer.getUniqueID());
        player.setHealth((double) forgePlayer.getHealth());
        player.setMaxHealth((double) forgePlayer.getMaxHealth());
        player.setExperienceLevel(forgePlayer.experienceLevel);
        player.setExperienceProgress((double) forgePlayer.experience);
        player.setTotalExperience(forgePlayer.experienceTotal);
        player.setOp(forgePlayer.mcServer.getConfigurationManager().func_152607_e(forgePlayer.getGameProfile()));
        player.setWalkSpeed((double) forgePlayer.capabilities.getWalkSpeed());
        player.setX(forgePlayer.posX);
        player.setY(forgePlayer.posY);
        player.setZ(forgePlayer.posZ);
        return player;
    }

    public static AchievementModel getForgeAchievement(PlatformContext context, String nickname, Achievement achievement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        achievementModel.setKey(achievement.statId);
        String frameType = achievement.getSpecial() ? "goal" : "task";
        displayModel.setFrame(frameType);
        displayModel.setTitle(context.parseTranslateModel(achievement.func_150951_e()));
        achievementModel.setDisplay(displayModel);

        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                new ChatComponentText(nickname),
                achievement.func_150951_e()
        );
        achievementModel.setTranslation(context.parseTranslateModel(chatComponentTranslation));

        return achievementModel;
    }

    /**
     * 调用原版方法反序列化 Json 文本组件。
     *
     * @param jsonElement 消息体
     * @return 文本组件
     */
    public static IChatComponent buildComponent(JsonElement jsonElement) {
        return IChatComponent.Serializer.func_150699_a(jsonElement.toString());
    }
}
