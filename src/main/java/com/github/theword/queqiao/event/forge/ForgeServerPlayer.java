package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.base.BasePlayer;
import com.google.gson.annotations.SerializedName;

public class ForgeServerPlayer extends BasePlayer {

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("ip_address")
    private String ipAddress;

    private float speed;
    @SerializedName("flying_speed")
    private float flyingSpeed;

    @SerializedName("is_flying")
    private boolean isFlying;

    @SerializedName("is_swimming")
    private boolean isSwimming;
    @SerializedName("is_sleeping")
    private boolean isSleeping;
    @SerializedName("is_blocking")
    private boolean isBlocking;

    @SerializedName("game_mode")
    private String gameMode;

    @SerializedName("block_x")
    private int blockX;
    @SerializedName("block_y")
    private int blockY;
    @SerializedName("block_z")
    private int blockZ;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getFlyingSpeed() {
        return flyingSpeed;
    }

    public void setFlyingSpeed(float flyingSpeed) {
        this.flyingSpeed = flyingSpeed;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public boolean isSwimming() {
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        isSwimming = swimming;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        isSleeping = sleeping;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getBlockX() {
        return blockX;
    }

    public void setBlockX(int blockX) {
        this.blockX = blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public void setBlockY(int blockY) {
        this.blockY = blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public void setBlockZ(int blockZ) {
        this.blockZ = blockZ;
    }
}