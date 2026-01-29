package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;

public class NeoForgeTool {
    public static PlayerModel getNeoForgePlayer(ServerPlayer neoForgePlayer) {
        PlayerModel player = new PlayerModel();
        player.setNickname(neoForgePlayer.getName().getString());
        player.setUuid(neoForgePlayer.getUUID());
        player.setAddress(neoForgePlayer.getIpAddress());
        player.setHealth((double) neoForgePlayer.getHealth());
        player.setMaxHealth((double) neoForgePlayer.getMaxHealth());
        player.setExperienceLevel(neoForgePlayer.experienceLevel);
        player.setExperienceProgress((double) neoForgePlayer.experienceProgress);
        player.setTotalExperience(neoForgePlayer.totalExperience);
        player.setOp(neoForgePlayer.hasPermissions(2));
        player.setWalkSpeed((double) neoForgePlayer.getAbilities().getWalkingSpeed());
        player.setX(neoForgePlayer.getX());
        player.setY(neoForgePlayer.getY());
        player.setZ(neoForgePlayer.getZ());
        return player;
    }

    public static AchievementModel getNeoForgeAchievement(String nickname, Advancement advancement) {
        AchievementModel achievement = new AchievementModel();
        if (advancement.display().isEmpty()) {
            return achievement;
        }
        DisplayInfo displayInfo = advancement.display().get();
        DisplayModel display = new DisplayModel();
        display.setTitle(parseTranslateModel(displayInfo.getTitle()));
        display.setDescription(parseTranslateModel(displayInfo.getDescription()));
        display.setFrame(displayInfo.getType().toString());
        achievement.setDisplay(display);

        String translationKey = achievement.getTranslationKey(displayInfo.getType().name());

        MutableComponent translatable = Component.translatable(
                translationKey,
                Component.literal(nickname),
                advancement.display().get().getTitle()
        );

        achievement.setTranslation(parseTranslateModel(translatable));

        return achievement;
    }

    public static TranslateModel parseTranslateModel(Component component) {
        if (!(component.getContents() instanceof TranslatableContents translatableTextContent))
            return new TranslateModel(null, null, component.getString());
        Object[] rawArgs = translatableTextContent.getArgs();
        TranslateModel[] childModels = new TranslateModel[rawArgs.length];
        String[] stringsForFormat = new String[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            Object rawArg = rawArgs[i];

            TranslateModel childModel = (rawArg instanceof Component componentArg) ? parseTranslateModel(componentArg)
                    : new TranslateModel(null, null, String.valueOf(rawArg));
            childModels[i] = childModel;
            stringsForFormat[i] = childModel.getText();
        }

        String finalText = GlobalContext.translate(translatableTextContent.getKey(), stringsForFormat);
        if (finalText.equals(translatableTextContent.getKey())) {
            finalText = component.getString();
        }
        return new TranslateModel(translatableTextContent.getKey(), childModels, finalText);
    }

}