package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.BasePlayerJoinEvent;

public final class ForgePlayerLoggedInEvent extends BasePlayerJoinEvent {
    public ForgePlayerLoggedInEvent(ForgeServerPlayer player) {
        super("PlayerLoggedInEvent", player);
    }
}