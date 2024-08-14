package com.github.theword.queqiao.event.velocity;

import com.github.theword.queqiao.tool.event.base.BasePlayer;
import com.google.gson.annotations.SerializedName;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.util.GameProfile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.InetSocketAddress;


@Data
@EqualsAndHashCode(callSuper = true)
public class VelocityPlayer extends BasePlayer {

    private long ping;
    @SerializedName("online_mode")
    private boolean onlineMode;
    @SerializedName("game_profile")
    private GameProfile gameProfile;
    @SerializedName("remote_address")
    private InetSocketAddress remoteAddress;
    @SerializedName("player_settings")
    private PlayerSettings playerSettings;

}
