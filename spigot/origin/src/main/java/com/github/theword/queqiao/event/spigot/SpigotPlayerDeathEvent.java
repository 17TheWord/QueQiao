package com.github.theword.queqiao.event.spigot;

import com.github.theword.queqiao.tool.event.BasePlayerDeathEvent;

public class SpigotPlayerDeathEvent extends BasePlayerDeathEvent {

    public SpigotPlayerDeathEvent(SpigotPlayer player, String message) {
        super("PlayerDeathEvent", "", player, message);
    }
}