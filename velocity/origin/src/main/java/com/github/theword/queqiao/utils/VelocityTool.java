package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.velocitypowered.api.proxy.Player;

public class VelocityTool {

    public static PlayerModel getVelocityPlayer(Player player) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(player.getUsername());
        playerModel.setUuid(player.getUniqueId());
        playerModel.setOp(player.hasPermission("minecraft.command.op"));
        return playerModel;
    }
}