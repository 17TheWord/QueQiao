package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.fabric.FabricServerPlayer;
import net.minecraft.server.network.ServerPlayerEntity;

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
}