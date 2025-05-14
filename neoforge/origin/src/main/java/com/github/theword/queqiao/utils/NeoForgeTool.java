package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.neoforge.NeoForgeServerPlayer;
import com.github.theword.queqiao.event.neoforge.dto.advancement.AdvancementRewardsDTO;
import com.github.theword.queqiao.event.neoforge.dto.advancement.DisplayInfoDTO;
import com.github.theword.queqiao.event.neoforge.dto.advancement.ItemStackDTO;
import com.github.theword.queqiao.event.neoforge.dto.advancement.NeoForgeAdvancement;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Collectors;

public class NeoForgeTool {
    public static NeoForgeServerPlayer getNeoForgePlayer(ServerPlayer player) {
        NeoForgeServerPlayer neoForgeServerPlayer = new NeoForgeServerPlayer();
        neoForgeServerPlayer.setNickname(player.getName().getString());

        Component playerDisplayName = player.getDisplayName();
        neoForgeServerPlayer.setDisplayName(playerDisplayName == null ? "" : playerDisplayName.getString());

        neoForgeServerPlayer.setUuid(player.getUUID());
        neoForgeServerPlayer.setIpAddress(player.getIpAddress());

        neoForgeServerPlayer.setSpeed(player.getSpeed());
        neoForgeServerPlayer.setGameMode(player.gameMode.getGameModeForPlayer().toString());

        neoForgeServerPlayer.setBlockX((int) player.getX());
        neoForgeServerPlayer.setBlockY((int) player.getY());
        neoForgeServerPlayer.setBlockZ((int) player.getZ());

        neoForgeServerPlayer.setSwimming(player.isSwimming());
        neoForgeServerPlayer.setSleeping(player.isSleeping());
        neoForgeServerPlayer.setBlocking(player.isBlocking());

        neoForgeServerPlayer.setFlying(player.getAbilities().flying);
        neoForgeServerPlayer.setFlyingSpeed(player.getAbilities().getFlyingSpeed());

        return neoForgeServerPlayer;
    }

    public static NeoForgeAdvancement getNeoForgeAdvancement(Advancement advancement) {
        NeoForgeAdvancement neoForgeAdvancement = new NeoForgeAdvancement();
        if (advancement.name().isPresent()) neoForgeAdvancement.setName(advancement.name().get().getString());
        if (advancement.parent().isPresent()) neoForgeAdvancement.setParent(advancement.parent().get().toString());

        if (advancement.display().isPresent()) {
            DisplayInfoDTO displayInfoDTO = new DisplayInfoDTO();
            displayInfoDTO.setTitle(advancement.display().get().getTitle().getString());
            displayInfoDTO.setDescription(advancement.display().get().getDescription().getString());

            ItemStack icon = advancement.display().get().getIcon();
            ItemStackDTO itemStackDTO = new ItemStackDTO();
            itemStackDTO.setCount(icon.getCount());
            itemStackDTO.setPopTime(icon.getPopTime());
            itemStackDTO.setItem(icon.getItem().toString());
            displayInfoDTO.setIcon(itemStackDTO);

            neoForgeAdvancement.setDisplay(displayInfoDTO);
        }

        AdvancementRewards rewards = advancement.rewards();
        AdvancementRewardsDTO advancementRewardsDTO = new AdvancementRewardsDTO();

        advancementRewardsDTO.setExperience(rewards.experience());

        advancementRewardsDTO.setLoot(
                rewards.loot().stream()
                        .map(ResourceKey::location)
                        .map(Object::toString)
                        .collect(Collectors.toList())
        );

        advancementRewardsDTO.setRecipes(
                rewards.recipes().stream()
                        .map(ResourceLocation::toString)
                        .collect(Collectors.toList())
        );

        neoForgeAdvancement.setRewards(advancementRewardsDTO);
        neoForgeAdvancement.setSendsTelemetryEvent(advancement.sendsTelemetryEvent());

        return neoForgeAdvancement;
    }
}