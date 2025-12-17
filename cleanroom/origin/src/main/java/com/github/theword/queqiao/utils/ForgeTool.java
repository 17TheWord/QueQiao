package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
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

    public static AchievementModel getForgeAchievement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        achievementModel.setKey(advancement.getId().toString());
        DisplayInfo displayInfo = advancement.getDisplay();
        displayModel.setFrame(displayInfo.getFrame().toString());
        displayModel.setTitle(((TextComponentTranslation) displayInfo.getTitle()).getKey());
        displayModel.setDescription(((TextComponentTranslation) displayInfo.getDescription()).getKey());
        achievementModel.setDisplay(displayModel);
        return achievementModel;
    }

    /** 调用原版方法反序列化Json文本组件, 并将异常输出到日志
     * @param jsonElement 消息体
     * @return 文本组件, 或null如果解析出错
     */
    public static ITextComponent parseJsonToTextWrapped(JsonElement jsonElement) {
        try {
            return ITextComponent.Serializer.fromJsonLenient(jsonElement.toString());
        } catch (Throwable e) {
            GlobalContext.getLogger().error("Error handling Broadcast Message {} : ", jsonElement, e);
        }
        return null;
    }
}