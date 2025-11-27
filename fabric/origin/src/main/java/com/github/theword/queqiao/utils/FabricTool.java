package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
// IF >= fabric-1.21.6
//import com.mojang.serialization.JsonOps;
//import net.minecraft.text.TextCodecs;
// ELSE
//import net.minecraft.text.Text;
// END IF

// IF < fabric-1.19
//import net.minecraft.text.TranslatableText;
// ELSE
//import net.minecraft.text.TranslatableTextContent;
// END IF

// IF >= fabric-1.21 && < fabric-1.21.6
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

    public static DisplayModel getFabricAchievementDisplay(AdvancementDisplay advancementDisplay) {
        DisplayModel displayModel = new DisplayModel();
        // IF < fabric-1.19
//        displayModel.setTitle(((TranslatableText) advancementDisplay.getTitle()).getKey());
//        displayModel.setDescription(((TranslatableText) advancementDisplay.getDescription()).getKey());
        // ELSE
//        displayModel.setTitle(((TranslatableTextContent) advancementDisplay.getTitle().getContent()).getKey());
//        displayModel.setDescription(((TranslatableTextContent) advancementDisplay.getDescription().getContent()).getKey());
        // END IF
        displayModel.setFrame(advancementDisplay.getFrame().toString());

        return displayModel;
    }

    public static MutableText buildComponent(JsonElement jsonElement) {
        // IF >= fabric-1.21.6
//        return TextCodecs.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow().getFirst().copy();
        // ELSE IF >= fabric-1.21
//        return Text.Serialization.fromJsonTree(jsonElement, minecraftServer.getRegistryManager()).copy();
        // ELSE IF >= fabric-1.20.4
//        return Text.Serialization.fromJsonTree(jsonElement).copy();
        // ELSE
//        return Text.Serializer.fromJson(jsonElement).copy();
        // END IF
    }
}