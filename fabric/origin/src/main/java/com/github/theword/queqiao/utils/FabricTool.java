package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.fabric.FabricServerPlayer;
import com.github.theword.queqiao.event.fabric.dto.advancement.*;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.network.ServerPlayerEntity;
// IF >= fabric-1.21
//import net.minecraft.registry.RegistryKey;
// END IF
// IF >= fabric-1.20.4
//import net.minecraft.util.Identifier;
//import java.util.stream.Collectors;
// END IF

public class FabricTool {

    public static FabricServerPlayer getFabricPlayer(ServerPlayerEntity player) {
        FabricServerPlayer fabricServerPlayer = new FabricServerPlayer();

        fabricServerPlayer.setNickname(player.getName().getString());
        fabricServerPlayer.setUuid(player.getUuid());
        fabricServerPlayer.setIp(player.getIp());
        fabricServerPlayer.setDisplayName(player.getDisplayName().getString());
        fabricServerPlayer.setMovementSpeed(player.getMovementSpeed());

        // IF > fabric-1.16.5
//        fabricServerPlayer.setBlockX(player.getBlockX());
//        fabricServerPlayer.setBlockY(player.getBlockY());
//        fabricServerPlayer.setBlockZ(player.getBlockZ());
        // ELSE
//        fabricServerPlayer.setBlockX((int) player.getX());
//        fabricServerPlayer.setBlockY((int) player.getY());
//        fabricServerPlayer.setBlockX((int) player.getZ());
        // END IF

        player.isCreative();
        player.isSpectator();
        player.isSneaking();
        player.isSleeping();
        player.isClimbing();
        player.isSwimming();

        return fabricServerPlayer;
    }

    public static FabricAdvancement getFabricAdvancement(Advancement advancement) {
        FabricAdvancement fabricAdvancement = new FabricAdvancement();
        // IF >= fabric-1.20.4
//        advancement.parent().ifPresent(parent -> fabricAdvancement.setParent(parent.toString()));
//        advancement.display().ifPresent(displayInfo -> {
//            AdvancementDisplayDTO advancementDisplayDTO = new AdvancementDisplayDTO();
//            advancementDisplayDTO.setTitle(displayInfo.getTitle().getString());
//            advancementDisplayDTO.setDescription(displayInfo.getDescription().getString());
//            displayInfo.getBackground().ifPresent(background -> advancementDisplayDTO.setBackground(background.toString()));
//            advancementDisplayDTO.setFrame(displayInfo.getFrame().getToastText().getString());
//            advancementDisplayDTO.setShowToast(displayInfo.shouldShowToast());
//            advancementDisplayDTO.setAnnounceToChat(displayInfo.shouldAnnounceToChat());
//            advancementDisplayDTO.setHidden(displayInfo.isHidden());
//            if (!displayInfo.getIcon().isEmpty()) {
//                ItemStackDTO itemStackDTO = new ItemStackDTO();
//                itemStackDTO.setCount(displayInfo.getIcon().getCount());
//                itemStackDTO.setName(displayInfo.getIcon().getName().getString());
//            }
//            fabricAdvancement.setDisplay(advancementDisplayDTO);
//        });
//        AdvancementRewardsDTO advancementRewardsDTO = new AdvancementRewardsDTO();
//        advancementRewardsDTO.setExperience(advancement.rewards().experience());
//        advancementRewardsDTO.setRecipes(advancement.rewards().recipes().stream().map(Identifier::toString).collect(Collectors.toList()));
//        advancementRewardsDTO.setRecipes(advancement.rewards().recipes().stream().map(Identifier::toString).collect(Collectors.toList()));
        // IF >= fabric-1.21
//        advancementRewardsDTO.setLoot(advancement.rewards().loot().stream().map(RegistryKey::toString).collect(Collectors.toList()));
        // ELSE
//        advancementRewardsDTO.setLoot(advancement.rewards().loot().stream().map(Identifier::toString).collect(Collectors.toList()));
        // END IF
//
//        fabricAdvancement.setSendsTelemetryEvent(advancement.sendsTelemetryEvent());
//        advancement.name().ifPresent(name -> fabricAdvancement.setName(name.getString()));
        // ELSE
//        fabricAdvancement.setParent(advancement.getParent() != null ? advancement.getParent().toString() : "");
//        if (advancement.getDisplay() != null) {
//            AdvancementDisplayDTO advancementDisplayDTO = new AdvancementDisplayDTO();
//            advancementDisplayDTO.setTitle(advancement.getDisplay().getTitle().getString());
//            advancementDisplayDTO.setDescription(advancement.getDisplay().getDescription().getString());
//            advancementDisplayDTO.setBackground(advancement.getDisplay().getBackground() != null ? advancement.getDisplay().getBackground().toString() : "");
//            advancementDisplayDTO.setFrame(advancement.getDisplay().getFrame().getToastText().getString());
//            advancementDisplayDTO.setShowToast(advancement.getDisplay().shouldShowToast());
//            advancementDisplayDTO.setAnnounceToChat(advancement.getDisplay().shouldAnnounceToChat());
//            advancementDisplayDTO.setHidden(advancement.getDisplay().isHidden());
//            advancementDisplayDTO.setX(advancement.getDisplay().getX());
//            advancementDisplayDTO.setY(advancement.getDisplay().getY());
//            if (!advancement.getDisplay().getIcon().isEmpty()) {
//                ItemStackDTO itemStackDTO = new ItemStackDTO();
//                itemStackDTO.setCount(advancement.getDisplay().getIcon().getCount());
//                itemStackDTO.setName(advancement.getDisplay().getIcon().getName().getString());
//            }
//            fabricAdvancement.setDisplay(advancementDisplayDTO);
//        }
//        fabricAdvancement.setRewards(advancement.getRewards().toJson());
//        fabricAdvancement.setId(advancement.getId().toString());
        // END IF
        return fabricAdvancement;
    }
}