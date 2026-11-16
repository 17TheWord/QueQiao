package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
// IF >= fabric-1.20.4
//import net.minecraft.advancement.AdvancementEntry;
// ELSE
//import net.minecraft.advancement.Advancement;
// END IF
// IF >= fabric-1.21.6
//import com.mojang.serialization.JsonOps;
//import net.minecraft.text.TextCodecs;
// END IF
import net.minecraft.text.Text;
// IF < fabric-1.19
//import net.minecraft.text.TranslatableText;
// END IF
// IF >= fabric-1.20.4
//import java.util.Optional;
// END IF

// IF >= fabric-1.21 && < fabric-1.21.6
//import static com.github.theword.queqiao.QueQiao.minecraftServer;
// END IF

// IF >= fabric-1.21.11
//import net.minecraft.command.permission.Permission;
//import net.minecraft.command.permission.PermissionLevel;
// ELSE
//import io.github.theword.queqiao.core.constant.CommandConstant;
// END IF

import net.minecraft.server.command.ServerCommandSource;

public class FabricTool {

    public static PlayerModel getFabricPlayer(ServerPlayerEntity fabricPlayer) {
        PlayerModel player = new PlayerModel();

        player.setNickname(fabricPlayer.getName().getString());
        player.setUuid(fabricPlayer.getUuid());
        player.setAddress(fabricPlayer.getIp());

        player.setHealth((double) fabricPlayer.getHealth());
        player.setMaxHealth((double) fabricPlayer.getMaxHealth());

        player.setExperienceLevel(fabricPlayer.experienceLevel);
        player.setExperienceProgress((double) fabricPlayer.experienceProgress);
        player.setTotalExperience(fabricPlayer.totalExperience);

        // IF >= fabric-1.21.11
//        player.setOp(fabricPlayer.getPermissions().hasPermission(new Permission.Level(PermissionLevel.ADMINS)));
        // ELSE
//        player.setOp(fabricPlayer.hasPermissionLevel(CommandConstant.MOD_PERMISSION_LEVEL));
        // END IF

        player.setWalkSpeed((double) fabricPlayer.getMovementSpeed());

        player.setX(fabricPlayer.getX());
        player.setY(fabricPlayer.getY());
        player.setZ(fabricPlayer.getZ());

        return player;
    }

    public static DisplayModel getFabricAchievementDisplay(PlatformContext context, AdvancementDisplay advancementDisplay) {
        DisplayModel displayModel = new DisplayModel();
        displayModel.setTitle(context.parseTranslateModel(advancementDisplay.getTitle()));
        displayModel.setDescription(context.parseTranslateModel(advancementDisplay.getDescription()));
        displayModel.setFrame(advancementDisplay.getFrame().toString());

        return displayModel;
    }

    /**
     * 构造成就模型；成就未配置公告或无法解析时返回 null
     */
    // IF < fabric-1.20.4
//    public static AchievementModel getFabricAchievement(PlatformContext context, ServerPlayerEntity player, Advancement advancement) {
//        AdvancementDisplay advancementDisplay = advancement.getDisplay();
//        if (advancementDisplay == null || !advancementDisplay.shouldAnnounceToChat()) {
//            return null;
//        }
//
//        AchievementModel achievementModel = new AchievementModel();
//        achievementModel.setKey(advancement.getId().toString());
//
//        DisplayModel displayModel = getFabricAchievementDisplay(context, advancementDisplay);
//        achievementModel.setDisplay(displayModel);
        // IF < fabric-1.19
//        Text translateText = new TranslatableText(achievementModel.getTranslationKey(displayModel.getFrame()), player.getDisplayName(), advancement.toHoverableText());
        // ELSE
//        Text translateText = Text.translatable(achievementModel.getTranslationKey(displayModel.getFrame()), player.getDisplayName(), advancement.toHoverableText());
        // END IF
//        achievementModel.setTranslation(context.parseTranslateModel(translateText));
//        return achievementModel;
//    }
    // ELSE
//    public static AchievementModel getFabricAchievement(PlatformContext context, ServerPlayerEntity player, AdvancementEntry advancementEntry) {
//        Optional<AdvancementDisplay> displayOptional = advancementEntry.value().display();
//        if (displayOptional.isEmpty() || !displayOptional.get().shouldAnnounceToChat()) {
//            return null;
//        }
//
//        AdvancementDisplay advancementDisplay = displayOptional.get();
//        AchievementModel achievementModel = new AchievementModel();
//        achievementModel.setKey(advancementEntry.id().toString());
//
//        DisplayModel displayModel = getFabricAchievementDisplay(context, advancementDisplay);
//        achievementModel.setDisplay(displayModel);
//
//        Text translateText = advancementDisplay.getFrame().getChatAnnouncementText(advancementEntry, player);
//        achievementModel.setTranslation(context.parseTranslateModel(translateText));
//        return achievementModel;
//    }
    // END IF

    public static MutableText buildComponent(JsonElement jsonElement) {
        // IF >= fabric-1.21.6
//        return TextCodecs.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow().getFirst().copy();
        // ELSE IF >= fabric-1.21
//        return Text.Serialization.fromJsonTree(jsonElement, minecraftServer.getRegistryManager()).copy();
        // ELSE IF >= fabric-1.20.4
//        return Text.Serialization.fromJsonTree(jsonElement).copy();
        // ELSE
//        return Text.Serializer.fromJson(jsonElement);
        // END IF
    }

    public static boolean permissionCheck(ServerCommandSource source) {
        // IF >= fabric-1.21.11
//        return source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.ADMINS));
        // ELSE
//        return source.hasPermissionLevel(CommandConstant.MOD_PERMISSION_LEVEL);
        // END IF
    }
}
