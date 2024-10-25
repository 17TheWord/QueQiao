package com.github.theword.queqiao.event.minecraft;

import com.github.theword.queqiao.tool.event.base.BasePlayerJoinEvent;

public class MinecraftPlayerJoinEvent extends BasePlayerJoinEvent {
    public MinecraftPlayerJoinEvent(MinecraftPlayer player) {
        super("MinecraftPlayerJoinEvent", player);
    }
}