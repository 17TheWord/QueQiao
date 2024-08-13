package com.github.theword.queqiao.event.spigot;

import com.github.theword.queqiao.tool.event.BasePlayerJoinEvent;

public class SpigotPlayerJoinEvent extends BasePlayerJoinEvent {

    public SpigotPlayerJoinEvent(SpigotPlayer player) {
        super("PlayerJoinEvent", player);
    }

}