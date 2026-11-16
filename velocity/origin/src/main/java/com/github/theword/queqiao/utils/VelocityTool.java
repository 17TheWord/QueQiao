package com.github.theword.queqiao.utils;

import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class VelocityTool {

    public static PlayerModel getVelocityPlayer(Player player) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(player.getUsername());
        playerModel.setUuid(player.getUniqueId());
        playerModel.setOp(player.hasPermission("minecraft.command.op"));
        return playerModel;
    }

    public static Component buildComponent(JsonElement jsonElement) {
        return GsonComponentSerializer.gson().deserializeFromTree(jsonElement);
    }
}
