package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;


public class ForgeTool {
    public static PlayerModel getForgePlayer(EntityPlayerMP forgeServerPlayer) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(forgeServerPlayer.getName());
        playerModel.setUuid(forgeServerPlayer.getUniqueID());
        playerModel.setAddress(forgeServerPlayer.getPlayerIP());
        playerModel.setHealth((double) forgeServerPlayer.getHealth());
        playerModel.setMaxHealth((double) forgeServerPlayer.getMaxHealth());
        playerModel.setExperienceLevel(forgeServerPlayer.experienceLevel);
        playerModel.setExperienceProgress((double) forgeServerPlayer.experience);
        playerModel.setTotalExperience(forgeServerPlayer.experienceTotal);
        playerModel.setOp(forgeServerPlayer.canUseCommand(4, "op"));
        playerModel.setWalkSpeed((double) forgeServerPlayer.getAIMoveSpeed());
        playerModel.setX(forgeServerPlayer.posX);
        playerModel.setY(forgeServerPlayer.posY);
        playerModel.setZ(forgeServerPlayer.posZ);
        return playerModel;
    }

    public static AchievementModel getForgeAchievement(PlatformContext context, String nickname, Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        if (advancement.getDisplay() == null) {
            return achievementModel;
        }
        achievementModel.setKey(advancement.getId().toString());
        DisplayInfo displayInfo = advancement.getDisplay();
        displayModel.setFrame(displayInfo.getFrame().toString());
        displayModel.setTitle(context.parseTranslateModel(displayInfo.getTitle()));
        displayModel.setDescription(context.parseTranslateModel(displayInfo.getDescription()));
        achievementModel.setDisplay(displayModel);

        TextComponentTranslation translation = new TextComponentTranslation(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                new TextComponentString(nickname),
                advancement.getDisplayText()
        );
        achievementModel.setTranslation(context.parseTranslateModel(translation));
        return achievementModel;
    }

    /**
     * 调用原版方法反序列化 Json 文本组件。
     *
     * @param jsonElement 消息体
     * @return 文本组件
     */
    public static ITextComponent buildComponent(JsonElement jsonElement) {
        return ITextComponent.Serializer.fromJsonLenient(jsonElement.toString());
    }
}
