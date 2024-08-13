package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.BasePlayer;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FabricServerPlayer extends BasePlayer {
    private String ip;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("movement_speed")
    private float movementSpeed;

    @SerializedName("block_x")
    private int blockX;
    @SerializedName("block_y")
    private int blockY;
    @SerializedName("block_z")
    private int blockZ;

    @SerializedName("is_creative")
    private boolean isCreative;
    @SerializedName("is_spectator")
    private boolean isSpectator;
    @SerializedName("is_sneaking")
    private boolean isSneaking;
    @SerializedName("is_sleeping")
    private boolean isSleeping;
    @SerializedName("is_climbing")
    private boolean isClimbing;
    @SerializedName("is_swimming")
    private boolean isSwimming;
}