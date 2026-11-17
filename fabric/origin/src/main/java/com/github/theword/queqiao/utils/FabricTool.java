package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
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

    public static DisplayModel getFabricAchievementDisplay(PlatformContext context, DisplayInfo advancementDisplay) {
        DisplayModel displayModel = new DisplayModel();
        // IF fabric-26.1
        //displayModel.setTitle(context.parseTranslateModel(advancementDisplay.getTitle()));
        //displayModel.setDescription(context.parseTranslateModel(advancementDisplay.getDescription()));
        //displayModel.setFrame(advancementDisplay.getType().toString());
        // ELSE IF fabric-26.3
        //displayModel.setTitle(context.parseTranslateModel(advancementDisplay.title()));
        //displayModel.setDescription(context.parseTranslateModel(advancementDisplay.description()));
        //displayModel.setFrame(advancementDisplay.type().toString());
        // END IF

        return displayModel;
    }

    public static AchievementModel getFabricAchievement(PlatformContext context, AdvancementHolder advancement) {
        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(advancement.id().toString());

        advancement.value().display().ifPresent(display -> {
            achievementModel.setDisplay(getFabricAchievementDisplay(context, display));
            // IF fabric-26.1
            //achievementModel.setTranslation(context.parseTranslateModel(display.getTitle()));
            // ELSE IF fabric-26.3
            //achievementModel.setTranslation(context.parseTranslateModel(display.title()));
            // END IF
        });

        return achievementModel;
    }

    public static MutableComponent buildComponent(JsonElement jsonElement) {
        return ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow().getFirst().copy();
    }

    public static boolean permissionCheck(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            return true;
        }
        return player.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.ADMINS));
    }
}
