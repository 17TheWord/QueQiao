package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.fabric.dto.advancement.AdvancementRewardsDTO;
import com.github.theword.queqiao.event.forge.ForgeServerPlayer;
import com.github.theword.queqiao.event.forge.dto.advancement.DisplayInfoDTO;
import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import net.minecraft.advancements.Advancement;

// IF > forge-1.16.5
//import com.github.theword.queqiao.event.forge.dto.advancement.ItemStackDTO;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.server.level.ServerPlayer;
//
//import java.util.stream.Collectors;
// ELSE
//import net.minecraft.entity.player.ServerPlayerEntity;
// END IF

public class ForgeTool {
    // IF > forge-1.16.5
//    public static ForgeServerPlayer getForgePlayer(ServerPlayer player) {
        // ELSE
//    public static ForgeServerPlayer getForgePlayer(ServerPlayerEntity player) {
        // END IF
        ForgeServerPlayer forgeServerPlayer = new ForgeServerPlayer();
        forgeServerPlayer.setNickname(player.getName().getString());
        forgeServerPlayer.setDisplayName(player.getDisplayName().getString());

        forgeServerPlayer.setUuid(player.getUUID());
        forgeServerPlayer.setIpAddress(player.getIpAddress());

        forgeServerPlayer.setSpeed(player.getSpeed());
        forgeServerPlayer.setGameMode(player.gameMode.getGameModeForPlayer().toString());

        forgeServerPlayer.setBlockX((int) player.getX());
        forgeServerPlayer.setBlockY((int) player.getY());
        forgeServerPlayer.setBlockZ((int) player.getZ());

        forgeServerPlayer.setSwimming(player.isSwimming());
        forgeServerPlayer.setSleeping(player.isSleeping());
        forgeServerPlayer.setBlocking(player.isBlocking());

        // IF > forge-1.16.5
//        forgeServerPlayer.setFlying(player.getAbilities().flying);
//        forgeServerPlayer.setFlyingSpeed(player.getAbilities().getFlyingSpeed());
        // ELSE
//        forgeServerPlayer.setFlying(player.abilities.flying);
//        forgeServerPlayer.setFlyingSpeed(player.abilities.getFlyingSpeed());
        // END IF

        return forgeServerPlayer;
    }

    public static ForgeAdvancement getForgeAdvancement(Advancement advancement) {
        ForgeAdvancement forgeAdvancement = new ForgeAdvancement();
        // IF >= forge-1.21
//        advancement.name().ifPresent(name -> forgeAdvancement.setName(name.getString()));
//        advancement.parent().ifPresent(parent -> forgeAdvancement.setParent(parent.toString()));
//        advancement.display().ifPresent(displayInfo -> {
//            DisplayInfoDTO displayInfoDTO = new DisplayInfoDTO();
//            displayInfoDTO.setTitle(displayInfo.getTitle().getString());
//            displayInfoDTO.setDescription(displayInfo.getDescription().getString());
//
//            ItemStack icon = displayInfo.getIcon();
//            ItemStackDTO itemStackDTO = new ItemStackDTO();
//            itemStackDTO.setCount(icon.getCount());
//            itemStackDTO.setPopTime(icon.getPopTime());
//            itemStackDTO.setDisplayName(icon.getDisplayName().getString());
//            itemStackDTO.setItem(icon.getItem().toString());
//            displayInfoDTO.setIcon(itemStackDTO);
//            forgeAdvancement.setDisplay(displayInfoDTO);
//        });
//        AdvancementRewardsDTO advancementRewardsDTO = new AdvancementRewardsDTO();
//        advancementRewardsDTO.setExperience(advancement.rewards().experience());
//        advancementRewardsDTO.setLoot(advancement.rewards().loot().stream().map(ResourceKey::toString).collect(Collectors.toList()));
//        advancementRewardsDTO.setRecipes(advancement.rewards().recipes().stream().map(ResourceLocation::toString).collect(Collectors.toList()));
//        forgeAdvancement.setRewards(advancementRewardsDTO);
        // ELSE
//        forgeAdvancement.setId(advancement.getId().toString());
//        forgeAdvancement.setParent(advancement.getParent() != null ? advancement.getParent().getId().toString() : null);
//        if (advancement.getDisplay() != null) {
//            DisplayInfoDTO displayInfoDTO = new DisplayInfoDTO();
//            displayInfoDTO.setTitle(advancement.getDisplay().getTitle().getString());
//            displayInfoDTO.setDescription(advancement.getDisplay().getDescription().getString());
        // IF > forge-1.16.5
//            ItemStack icon = advancement.getDisplay().getIcon();
//            ItemStackDTO itemStackDTO = new ItemStackDTO();
//            itemStackDTO.setCount(icon.getCount());
//            itemStackDTO.setPopTime(icon.getPopTime());
//            itemStackDTO.setItem(icon.getItem().toString());
//            displayInfoDTO.setIcon(itemStackDTO);
        // END IF
//            forgeAdvancement.setDisplay(displayInfoDTO);
//        }
//        forgeAdvancement.setRewards(advancement.getRewards().serializeToJson());
//        forgeAdvancement.setChatComponent(advancement.getChatComponent().getString());
//        forgeAdvancement.setText(advancement.getChatComponent().getString());
// END IF
        // IF >= forge-1.20
//        forgeAdvancement.setSendsTelemetryEvent(advancement.sendsTelemetryEvent());
        // END IF
        return forgeAdvancement;
    }
}