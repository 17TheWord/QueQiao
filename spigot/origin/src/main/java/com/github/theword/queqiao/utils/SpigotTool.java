package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.spigot.SpigotPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SpigotTool {
    /**
     * 获取SpigotPlayer
     *
     * @param player 玩家
     * @return SpigotPlayer
     */
    public static SpigotPlayer getSpigotPlayer(Player player) {
        SpigotPlayer spigotPlayer = new SpigotPlayer();
        spigotPlayer.setUuid(player.getUniqueId());
        spigotPlayer.setNickname(player.getName());
        spigotPlayer.setDisplayName(player.getDisplayName());
        spigotPlayer.setPlayerListName(player.getDisplayName());
        spigotPlayer.setAddress((Objects.requireNonNull(player.getAddress()).toString()));
        spigotPlayer.setHealthScale(player.getHealthScale());
        spigotPlayer.setExp(player.getExp());
        spigotPlayer.setTotalExp(player.getTotalExperience());
        spigotPlayer.setLevel(player.getLevel());
        spigotPlayer.setLocale(player.getLocale());
        // IF spigot-1.12.2
//        spigotPlayer.setPing(-1);
        // ELSE
//        spigotPlayer.setPing(player.getPing());
        // END IF
        spigotPlayer.setPlayerTime(player.getPlayerTime());
        spigotPlayer.setPlayerTimeRelative(player.isPlayerTimeRelative());
        spigotPlayer.setPlayerTimeOffset(player.getPlayerTimeOffset());
        spigotPlayer.setWalkSpeed(player.getWalkSpeed());
        spigotPlayer.setFlySpeed(player.getFlySpeed());
        spigotPlayer.setAllowFlight(player.getAllowFlight());
        spigotPlayer.setSprinting(player.isSprinting());
        spigotPlayer.setSneaking(player.isSneaking());
        spigotPlayer.setFlying(player.isFlying());
        spigotPlayer.setOp(player.isOp());
        return spigotPlayer;
    }
}