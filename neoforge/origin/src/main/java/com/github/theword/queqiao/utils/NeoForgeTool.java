package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.PlatformContext;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import static com.github.theword.queqiao.QueQiao.minecraftServer;

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

    public static AchievementModel getNeoForgeAchievement(PlatformContext context, String nickname, Advancement advancement) {
        AchievementModel achievement = new AchievementModel();
        if (advancement.display().isEmpty()) {
            return achievement;
        }

        DisplayInfo displayInfo = advancement.display().get();
        DisplayModel display = new DisplayModel();
        display.setTitle(context.parseTranslateModel(displayInfo.getTitle()));
        display.setDescription(context.parseTranslateModel(displayInfo.getDescription()));
        display.setFrame(displayInfo.getType().toString());
        achievement.setDisplay(display);

        String translationKey = achievement.getTranslationKey(displayInfo.getType().name());

        MutableComponent translatable = Component.translatable(
                translationKey,
                Component.literal(nickname),
                advancement.display().get().getTitle()
        );

        achievement.setTranslation(context.parseTranslateModel(translatable));

        return achievement;
    }

    public static MutableComponent buildComponent(JsonElement jsonElement) {
        // copy() 不可省略：Serializer 产出的组件，其 siblings 是不可变集合，
        // 后续 append（如拼接消息前缀）会抛 UnsupportedOperationException。
        // fabric 侧同样依赖 copy() 拿到可变实例。
        return Component.Serializer.fromJson(jsonElement, minecraftServer.registryAccess()).copy();
    }
}