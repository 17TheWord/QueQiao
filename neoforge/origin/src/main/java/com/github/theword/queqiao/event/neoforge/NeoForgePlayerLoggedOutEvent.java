package com.github.theword.queqiao.event.neoforge;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public final class NeoForgePlayerLoggedOutEvent extends BasePlayerQuitEvent {
    public NeoForgePlayerLoggedOutEvent(NeoForgeServerPlayer player) {
        super("NeoPlayerLoggedOutEvent", player);
    }
}