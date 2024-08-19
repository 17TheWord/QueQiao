package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.forge.ForgeServerPlayer;
// IF > forge-1.16.5
//import net.minecraft.server.level.ServerPlayer;
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
}