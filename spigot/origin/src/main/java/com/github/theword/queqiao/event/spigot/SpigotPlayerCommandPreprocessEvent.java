package com.github.theword.queqiao.event.spigot;

import com.github.theword.queqiao.tool.event.BaseCommandEvent;

public class SpigotPlayerCommandPreprocessEvent extends BaseCommandEvent {

    public SpigotPlayerCommandPreprocessEvent(SpigotPlayer player, String command) {
        super("PlayerCommandPreprocessEvent", "", player, command);
    }
}