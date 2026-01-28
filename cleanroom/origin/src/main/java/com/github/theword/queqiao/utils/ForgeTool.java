package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
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

    public static AchievementModel getForgeAchievement(String nickname, Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        achievementModel.setKey(advancement.getId().toString());
        DisplayInfo displayInfo = advancement.getDisplay();
        displayModel.setFrame(displayInfo.getFrame().toString());
        displayModel.setTitle(parseTranslateModel(displayInfo.getTitle()));
        displayModel.setDescription(parseTranslateModel(displayInfo.getTitle()));
        achievementModel.setDisplay(displayModel);

        TextComponentTranslation translation = new TextComponentTranslation(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                new TextComponentString(nickname),
                advancement.getDisplayText()
        );
        achievementModel.setTranslation(parseTranslateModel(translation));
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

    public static TranslateModel parseTranslateModel(ITextComponent iTextComponent) {
        if (!(iTextComponent instanceof TextComponentTranslation))
            return new TranslateModel(null, null, iTextComponent.getUnformattedText());

        TextComponentTranslation textComponentTranslation = (TextComponentTranslation) iTextComponent;
        Object[] rawArgs = textComponentTranslation.getFormatArgs();
        TranslateModel[] childModels = new TranslateModel[rawArgs.length];
        String[] stringsForFormat = new String[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            Object rawArg = rawArgs[i];
            TranslateModel childModel;
            if (rawArg instanceof ITextComponent) {
                childModel = parseTranslateModel((ITextComponent) rawArg);
            } else {
                childModel = new TranslateModel(null, null, String.valueOf(rawArg));
            }
            childModels[i] = childModel;
            stringsForFormat[i] = childModel.getText();
        }

        String finalText = GlobalContext.translate(textComponentTranslation.getKey(), stringsForFormat);
        if (finalText.equals(textComponentTranslation.getKey())) {
            finalText = textComponentTranslation.getUnformattedText();
        }
        return new TranslateModel(textComponentTranslation.getKey(), childModels, finalText);
    }

}