package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;

// IF > forge-1.16.5
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.server.level.ServerPlayer;
// ELSE
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.util.text.IFormattableTextComponent;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
// END IF

// IF forge-1.18.2
//import net.minecraft.network.chat.TranslatableComponent;
// END IF

// IF >= forge-1.21
//import static com.github.theword.queqiao.QueQiao.minecraftServer;
// END IF

public class ForgeTool {

    // IF > forge-1.16.5
//    public static PlayerModel getForgePlayer(ServerPlayer forgeServerPlayer) {
        // ELSE
//    public static PlayerModel getForgePlayer(ServerPlayerEntity forgeServerPlayer) {
        // END IF
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(forgeServerPlayer.getName().getString());
        playerModel.setUuid(forgeServerPlayer.getUUID());
        playerModel.setAddress(forgeServerPlayer.getIpAddress());
        playerModel.setHealth((double) forgeServerPlayer.getHealth());
        playerModel.setMaxHealth((double) forgeServerPlayer.getMaxHealth());
        playerModel.setExperienceLevel(forgeServerPlayer.experienceLevel);
        playerModel.setExperienceProgress((double) forgeServerPlayer.experienceProgress);
        playerModel.setTotalExperience(forgeServerPlayer.totalExperience);
        playerModel.setOp(forgeServerPlayer.hasPermissions(2));
        playerModel.setWalkSpeed((double) forgeServerPlayer.getSpeed());
        playerModel.setX(forgeServerPlayer.getX());
        playerModel.setY(forgeServerPlayer.getY());
        playerModel.setZ(forgeServerPlayer.getZ());
        return playerModel;
    }

    /**
     * 构造成就模型；成就未配置公告或无法解析时返回 null
     */
    public static AchievementModel getForgeAchievement(PlatformContext context, String nickname, Advancement advancement) {
        // IF >= forge-1.21
//        if (advancement.display().isEmpty() || !advancement.display().get().shouldAnnounceChat() || advancement.name().isEmpty()) {
//            return null;
//        }
//        DisplayInfo displayInfo = advancement.display().get();
        // ELSE
//        if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceChat()) {
//            return null;
//        }
//        DisplayInfo displayInfo = advancement.getDisplay();
        // END IF

        AchievementModel achievementModel = new AchievementModel();
        // IF >= forge-1.21
//        if (advancement.parent().isPresent()) {
//            achievementModel.setKey(advancement.parent().get().toString());
//        }
        // ELSE
//        achievementModel.setKey(advancement.getId().toString());
        // END IF

        DisplayModel displayModel = new DisplayModel();
        // IF >= forge-1.21
//        displayModel.setFrame(displayInfo.getType().toString());
        // ELSE
//        displayModel.setFrame(displayInfo.getFrame().toString());
        // END IF

        displayModel.setTitle(context.parseTranslateModel(displayInfo.getTitle()));
        displayModel.setDescription(context.parseTranslateModel(displayInfo.getDescription()));
        achievementModel.setDisplay(displayModel);

        // IF >= forge-1.21
//        MutableComponent translatable = Component.translatable(
//                achievementModel.getTranslationKey(displayModel.getFrame()),
//                Component.literal(nickname),
//                advancement.name().get()
//        );
        // ELSE IF >= forge-1.19
//        MutableComponent translatable = Component.translatable(
//                achievementModel.getTranslationKey(displayModel.getFrame()),
//                Component.literal(nickname),
//                advancement.getChatComponent()
//        );
        // ELSE IF forge-1.18.2
//        TranslatableComponent translatable = new TranslatableComponent(
//                achievementModel.getTranslationKey(displayModel.getFrame()),
//                new TranslatableComponent(nickname),
//                advancement.getDisplay().getTitle()
//        );
        // ELSE
//        TranslationTextComponent translatable = new TranslationTextComponent(
//                achievementModel.getTranslationKey(displayModel.getFrame()),
//                new TranslationTextComponent(nickname),
//                advancement.getDisplay().getTitle()
//        );
        // END IF
        achievementModel.setTranslation(context.parseTranslateModel(translatable));

        return achievementModel;
    }

    // IF >= forge-1.21
//    public static MutableComponent buildComponent(JsonElement jsonElement) {
//        return Component.Serializer.fromJson(jsonElement, minecraftServer.registryAccess());
//    }
    // ELSE IF >= forge-1.18
//    public static MutableComponent buildComponent(JsonElement jsonElement) {
//        return Component.Serializer.fromJson(jsonElement);
//    }
    // ELSE
//    public static IFormattableTextComponent buildComponent(JsonElement jsonElement) {
//        return ITextComponent.Serializer.fromJson(jsonElement);
//    }
    // END IF
}
