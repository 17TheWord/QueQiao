package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.neoforge.NeoForgeServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
}