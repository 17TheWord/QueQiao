package com.github.theword.queqiao.utils;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.EntityPlayerMP;

import com.github.theword.queqiao.event.forge.ForgeServerPlayer;
import net.minecraft.util.IChatComponent;

public class ForgeTool {

    public static ForgeServerPlayer getForgePlayer(EntityPlayerMP player) {
        ForgeServerPlayer forgeServerPlayer = new ForgeServerPlayer();
        forgeServerPlayer.setNickname(player.getDisplayName());
        forgeServerPlayer.setUuid(player.getUniqueID());
        forgeServerPlayer.setDisplayName(player.getDisplayName());
        forgeServerPlayer.setIpAddress(player.getPlayerIP());

        forgeServerPlayer.setSpeed(player.capabilities.getWalkSpeed());
        forgeServerPlayer.setFlyingSpeed(player.capabilities.getFlySpeed());

        forgeServerPlayer.setGameMode(
            player.theItemInWorldManager.getGameType()
                .getName());
        forgeServerPlayer.setFlying(player.capabilities.isFlying);
        forgeServerPlayer.setSleeping(player.isPlayerSleeping());
        forgeServerPlayer.setBlocking(player.isBlocking());

        forgeServerPlayer.setBlockX((int) player.posX);
        forgeServerPlayer.setBlockY((int) player.posY);
        forgeServerPlayer.setBlockZ((int) player.posZ);
        return forgeServerPlayer;
    }

    public static IChatComponent buildComponent(JsonElement jsonElement) {
        return IChatComponent.Serializer.func_150699_a(jsonElement.toString());
    }

}
