package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

// IF >= fabric-1.21.6
//import net.minecraft.text.TextCodecs;
//import com.mojang.serialization.JsonOps;
// ELSE
//import net.minecraft.text.Text;
// END IF

// IF >= fabric-1.21
//import static com.github.theword.queqiao.QueQiao.minecraftServer;
// END IF

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

        player.setOp(fabricPlayer.hasPermissionLevel(4));

        player.setWalkSpeed((double) fabricPlayer.getMovementSpeed());

        player.setX(fabricPlayer.getX());
        player.setY(fabricPlayer.getY());
        player.setZ(fabricPlayer.getZ());

        return player;
    }

    public static AchievementModel getFabricAchievement(Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        DisplayModel displayModel = new DisplayModel();

        // IF < fabric-1.20.4
//        AdvancementDisplay advancementDisplay = advancement.getDisplay();
//        if (advancementDisplay == null) {
//            achievementModel.setDisplay(displayModel);
//            return achievementModel;
//        }
        // ELSE
//        if (advancement.display().isEmpty()) {
//            achievementModel.setDisplay(displayModel);
//            return achievementModel;
//        }
//        AdvancementDisplay advancementDisplay = advancement.display().get();
        // END IF

        displayModel.setAnnounceChat(advancementDisplay.shouldAnnounceToChat());
        // IF < fabric-1.20.4
//        displayModel.setBackground(advancementDisplay.getBackground() == null ? null : advancementDisplay.getBackground().toString());
        // ELSE
//        displayModel.setBackground(advancementDisplay.getBackground().isEmpty() ? null : advancementDisplay.getBackground().toString());
        // END IF
        displayModel.setDescription(advancementDisplay.getDescription().getString());
        displayModel.setFrame(advancementDisplay.getFrame().toString());
        displayModel.setHidden(advancementDisplay.isHidden());
        displayModel.setIcon(advancementDisplay.getIcon().toString());
        displayModel.setShowToast(advancementDisplay.shouldShowToast());
        displayModel.setTitle(advancementDisplay.getTitle().getString());
        displayModel.setX((double) advancementDisplay.getX());
        displayModel.setY((double) advancementDisplay.getY());
        return achievementModel;
    }

    public static MutableText buildComponent(JsonElement jsonElement) {
        // IF >= fabric-1.21.6
//        return TextCodecs.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow().getFirst().copy();
        // ELSE IF >= fabric-1.21
//        return Text.Serialization.fromJsonTree(jsonElement, minecraftServer.getRegistryManager());
        // ELSE IF >= fabric-1.20.4
//        return Text.Serialization.fromJsonTree(jsonElement);
        // ELSE
//        return Text.Serializer.fromJson(jsonElement);
        // END IF
    }
}