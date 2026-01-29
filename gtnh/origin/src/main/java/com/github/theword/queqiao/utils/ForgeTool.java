package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.minecraft.entity.player.EntityPlayerMP;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ForgeTool {

    public static PlayerModel getPlayerModel(EntityPlayerMP forgePlayer) {
        PlayerModel player = new PlayerModel();
        player.setNickname(forgePlayer.getDisplayName());
        player.setUuid(forgePlayer.getUniqueID());
//        player.setAddress(forgePlayer.getPlayerIP());
        player.setHealth((double) forgePlayer.getHealth());
        player.setMaxHealth((double) forgePlayer.getMaxHealth());
        player.setExperienceLevel(player.getExperienceLevel());
        player.setExperienceProgress((double) forgePlayer.experience);
        player.setTotalExperience(forgePlayer.experienceTotal);
        player.setOp(forgePlayer.mcServer.getConfigurationManager().func_152607_e(forgePlayer.getGameProfile()));
        player.setWalkSpeed((double) forgePlayer.capabilities.getWalkSpeed());
        player.setX(forgePlayer.posX);
        player.setY(forgePlayer.posY);
        player.setZ(forgePlayer.posZ);
        return player;
    }

    public static AchievementModel getForgeAchievement(String nickname, Achievement achievement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        achievementModel.setKey(achievement.statId);
        String frameType = achievement.getSpecial() ? "goal" : "task";
        displayModel.setFrame(frameType);
        displayModel.setTitle(parseTranslateModel(achievement.func_150951_e()));
//        displayModel.setDescription(achievement.getDescription());  // Client Only
        achievementModel.setDisplay(displayModel);

        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                new ChatComponentText(nickname),
                achievement.func_150951_e()
        );
        achievementModel.setTranslation(parseTranslateModel(chatComponentTranslation));

        return achievementModel;
    }

    public static IChatComponent buildComponent(JsonElement jsonElement) {
        return IChatComponent.Serializer.func_150699_a(jsonElement.toString());
    }

    public static TranslateModel parseTranslateModel(IChatComponent iChatComponent) {
        if (!(iChatComponent instanceof ChatComponentTranslation chatComponentTranslation))
            return new TranslateModel(null, null, iChatComponent.getUnformattedText());

        Object[] rawArgs = chatComponentTranslation.getFormatArgs();
        TranslateModel[] childModels = new TranslateModel[rawArgs.length];
        String[] stringsForFormat = new String[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            Object rawArg = rawArgs[i];
            TranslateModel childModel;
            if (rawArg instanceof IChatComponent) {
                childModel = parseTranslateModel((IChatComponent) rawArg);
            } else {
                childModel = new TranslateModel(null, null, String.valueOf(rawArg));
            }
            childModels[i] = childModel;
            stringsForFormat[i] = childModel.getText();
        }

        String finalText = GlobalContext.translate(chatComponentTranslation.getKey(), stringsForFormat);
        if (finalText.equals(chatComponentTranslation.getKey())) {
            finalText = chatComponentTranslation.getUnformattedText();
        }
        return new TranslateModel(chatComponentTranslation.getKey(), childModels, finalText);
    }
}