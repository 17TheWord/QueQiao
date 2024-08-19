package com.github.theword.queqiao.event.spigot;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public class SpigotPlayerQuitEvent extends BasePlayerQuitEvent {

    public SpigotPlayerQuitEvent(SpigotPlayer player) {
        super("PlayerQuitEvent", player);
    }
}