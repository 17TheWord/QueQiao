package com.github.theword.queqiao.event.minecraft;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public class MinecraftPlayerQuitEvent extends BasePlayerQuitEvent {
    public MinecraftPlayerQuitEvent(MinecraftPlayer player) {
        super("MinecraftPlayerQuitEvent", player);
    }

}