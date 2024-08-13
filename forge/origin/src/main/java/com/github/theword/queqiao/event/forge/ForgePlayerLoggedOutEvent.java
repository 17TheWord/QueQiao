package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public final class ForgePlayerLoggedOutEvent extends BasePlayerQuitEvent {
    public ForgePlayerLoggedOutEvent(ForgeServerPlayer player) {
        super("PlayerLoggedOutEvent", player);
    }
}