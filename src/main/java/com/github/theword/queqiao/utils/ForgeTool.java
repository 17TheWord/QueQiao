package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.forge.ForgeServerPlayer;
import com.github.theword.queqiao.event.forge.dto.advancement.DisplayInfoDTO;
import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class ForgeTool {
    public static ForgeServerPlayer getForgePlayer(EntityPlayerMP player) {
        ForgeServerPlayer forgeServerPlayer = new ForgeServerPlayer();

        forgeServerPlayer.setNickname(player.getName());
        forgeServerPlayer.setDisplayName(player.getDisplayNameString());

        forgeServerPlayer.setUuid(player.getGameProfile().getId());
        forgeServerPlayer.setIpAddress(player.getPlayerIP());

        forgeServerPlayer.setSpeed(player.capabilities.getWalkSpeed());
        forgeServerPlayer.setFlyingSpeed(player.capabilities.getFlySpeed());

        forgeServerPlayer.setGameMode(player.interactionManager.getGameType().getName());
        forgeServerPlayer.setFlying(player.capabilities.isFlying);
        forgeServerPlayer.setSleeping(player.isPlayerSleeping());
        forgeServerPlayer.setBlocking(player.isActiveItemStackBlocking());

        BlockPos position = player.getPosition();
        forgeServerPlayer.setBlockX(position.getX());
        forgeServerPlayer.setBlockY(position.getY());
        forgeServerPlayer.setBlockZ(position.getZ());

        return forgeServerPlayer;
    }
    public static ForgeAdvancement getForgeAdvancement(Advancement advancement) {
        ForgeAdvancement forgeAdvancement = new ForgeAdvancement();
        forgeAdvancement.setId(advancement.getId().toString());
        forgeAdvancement.setParent(advancement.getParent() != null ? advancement.getParent().getId().toString() : null);
        if (advancement.getDisplay() != null) {
            DisplayInfoDTO displayInfoDTO = new DisplayInfoDTO();
            displayInfoDTO.setTitle(advancement.getDisplay().getTitle().getFormattedText());
            displayInfoDTO.setDescription(advancement.getDisplay().getDescription().getFormattedText());

            forgeAdvancement.setDisplay(displayInfoDTO);
        }
//        forgeAdvancement.setRewards(advancement.getRewards());
        forgeAdvancement.setChatComponent(advancement.getDisplayText().getFormattedText());
        forgeAdvancement.setText(advancement.getDisplayText().getFormattedText());

        return forgeAdvancement;
    }

}
