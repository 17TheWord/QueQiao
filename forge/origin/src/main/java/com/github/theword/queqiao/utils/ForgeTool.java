package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

public class ForgeTool {
    public static PlayerModel getForgePlayer(ServerPlayer forgeServerPlayer) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(forgeServerPlayer.getName().getString());
        playerModel.setUuid(forgeServerPlayer.getUUID());
        playerModel.setAddress(forgeServerPlayer.getIpAddress());
        playerModel.setHealth((double) forgeServerPlayer.getHealth());
        playerModel.setMaxHealth((double) forgeServerPlayer.getMaxHealth());
        playerModel.setExperienceLevel(forgeServerPlayer.experienceLevel);
        playerModel.setExperienceProgress((double) forgeServerPlayer.experienceProgress);
        playerModel.setTotalExperience(forgeServerPlayer.totalExperience);
        playerModel.setOp(forgeServerPlayer.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.ADMINS)));
        playerModel.setWalkSpeed((double) forgeServerPlayer.getSpeed());
        playerModel.setX(forgeServerPlayer.getX());
        playerModel.setY(forgeServerPlayer.getY());
        playerModel.setZ(forgeServerPlayer.getZ());
        return playerModel;
    }

    public static AchievementModel getForgeAchievement(String nickname, Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();
        if (advancement.display().isEmpty()) {
            return achievementModel;
        }
        if (advancement.parent().isPresent()) {
            achievementModel.setKey(advancement.parent().get().toString());
        }
        DisplayInfo displayInfo = advancement.display().get();
        displayModel.setFrame(displayInfo.getType().toString());

        displayModel.setTitle(parseTranslateModel(displayInfo.getTitle()));
        displayModel.setDescription(parseTranslateModel(displayInfo.getDescription()));
        achievementModel.setDisplay(displayModel);

        MutableComponent translatable = Component.translatable(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                Component.literal(nickname),
                advancement.name().get()
        );
        achievementModel.setTranslation(parseTranslateModel(translatable));

        return achievementModel;
    }

    public static MutableComponent buildComponent(JsonElement jsonElement) {
        return ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow().getFirst().copy();
    }

    public static boolean permissionCheck(CommandSourceStack source) {
        try {
            ServerPlayer playerOrException = source.getPlayerOrException();
            return playerOrException.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.ADMINS));
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static TranslateModel parseTranslateModel(Component component) {
        if (!(component.getContents() instanceof TranslatableContents translatableTextContent))
            return new TranslateModel(null, null, component.getString());
        Object[] rawArgs = translatableTextContent.getArgs();
        TranslateModel[] childModels = new TranslateModel[rawArgs.length];
        String[] stringsForFormat = new String[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            Object rawArg = rawArgs[i];

            TranslateModel childModel = (rawArg instanceof Component) ? parseTranslateModel((Component) rawArg)
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