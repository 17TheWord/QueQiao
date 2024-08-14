package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.velocity.VelocityPlayer;
import com.velocitypowered.api.proxy.Player;

public class VelocityTool {

    public static VelocityPlayer getVelocityPlayer(Player player) {
        VelocityPlayer velocityPlayer = new VelocityPlayer();

        velocityPlayer.setNickname(player.getUsername());
        velocityPlayer.setUuid(player.getUniqueId());
        velocityPlayer.setPing(player.getPing());
        velocityPlayer.setOnlineMode(player.isOnlineMode());

        velocityPlayer.setGameProfile(player.getGameProfile());
        velocityPlayer.setRemoteAddress(player.getRemoteAddress());
        velocityPlayer.setPlayerSettings(player.getPlayerSettings());
        return velocityPlayer;
    }
}
