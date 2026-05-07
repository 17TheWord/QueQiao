package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

public class FabricTool {

    public static PlayerModel getFabricPlayer(ServerPlayer fabricPlayer) {
        PlayerModel player = new PlayerModel();

        player.setNickname(fabricPlayer.getName().getString());
        player.setUuid(fabricPlayer.getUUID());
        player.setAddress(fabricPlayer.getIpAddress());

        player.setHealth((double) fabricPlayer.getHealth());
        player.setMaxHealth((double) fabricPlayer.getMaxHealth());

        player.setExperienceLevel(fabricPlayer.experienceLevel);
        player.setExperienceProgress((double) fabricPlayer.experienceProgress);
        player.setTotalExperience(fabricPlayer.totalExperience);

        Permission.HasCommandLevel permissionLevel = new Permission.HasCommandLevel(PermissionLevel.ADMINS);

        player.setOp(fabricPlayer.permissions().hasPermission(permissionLevel));

        player.setWalkSpeed((double) fabricPlayer.getSpeed());

        player.setX(fabricPlayer.getX());
        player.setY(fabricPlayer.getY());
        player.setZ(fabricPlayer.getZ());

        return player;
    }

    public static DisplayModel getFabricAchievementDisplay(DisplayInfo advancementDisplay) {
        DisplayModel displayModel = new DisplayModel();
        displayModel.setTitle(parseTranslateModel(advancementDisplay.getTitle()));
        displayModel.setDescription(parseTranslateModel(advancementDisplay.getDescription()));
        displayModel.setFrame(advancementDisplay.getType().toString());

        return displayModel;
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

    public static TranslateModel parseTranslateModel(Component text) {
        if (!(text.getContents() instanceof TranslatableContents translatableTextContent))
            return new TranslateModel(null, null, text.getString());

        Object[] rawArgs = translatableTextContent.getArgs();
        TranslateModel[] childModels = new TranslateModel[rawArgs.length];
        String[] stringsForFormat = new String[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            Object rawArg = rawArgs[i];
            TranslateModel childModel;
            if (rawArg instanceof Component) {
                childModel = parseTranslateModel((Component) rawArg);
            } else {
                childModel = new TranslateModel(null, null, String.valueOf(rawArg));
            }
            childModels[i] = childModel;
            stringsForFormat[i] = childModel.getText();
        }

        String finalText = GlobalContext.translate(translatableTextContent.getKey(), stringsForFormat);
        if (finalText.equals(translatableTextContent.getKey())) {
            finalText = text.getString();
        }
        return new TranslateModel(translatableTextContent.getKey(), childModels, finalText);
    }
}