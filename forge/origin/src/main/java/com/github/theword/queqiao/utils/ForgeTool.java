package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
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

    public static AchievementModel getForgeAchievement(PlatformContext context, String nickname, Advancement advancement) {
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

        displayModel.setTitle(context.parseTranslateModel(displayInfo.getTitle()));
        displayModel.setDescription(context.parseTranslateModel(displayInfo.getDescription()));
        achievementModel.setDisplay(displayModel);

        MutableComponent translatable = Component.translatable(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                Component.literal(nickname),
                advancement.name().get()
        );
        achievementModel.setTranslation(context.parseTranslateModel(translatable));

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
}
