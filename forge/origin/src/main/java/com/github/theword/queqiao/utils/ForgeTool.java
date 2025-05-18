package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.forge.ForgeServerPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ForgeTool {
    public static ForgeServerPlayer getForgePlayer(EntityPlayerMP player) {
        ForgeServerPlayer forgeServerPlayer = new ForgeServerPlayer();
        forgeServerPlayer.setNickname(player.getName());
        forgeServerPlayer.setUuid(player.getUniqueID());
        forgeServerPlayer.setDisplayName(player.getDisplayNameString());
        forgeServerPlayer.setIpAddress(player.getPlayerIP());

        forgeServerPlayer.setSpeed(player.capabilities.getWalkSpeed());
        forgeServerPlayer.setFlyingSpeed(player.capabilities.getFlySpeed());

        forgeServerPlayer.setGameMode(player.interactionManager.getGameType().getName());
        forgeServerPlayer.setFlying(player.capabilities.isFlying);
        forgeServerPlayer.setSleeping(player.isPlayerSleeping());
        forgeServerPlayer.setBlocking(player.isActiveItemStackBlocking());

        forgeServerPlayer.setBlockX(player.getPosition().getX());
        forgeServerPlayer.setBlockY(player.getPosition().getY());
        forgeServerPlayer.setBlockZ(player.getPosition().getZ());
        return forgeServerPlayer;
    }
}