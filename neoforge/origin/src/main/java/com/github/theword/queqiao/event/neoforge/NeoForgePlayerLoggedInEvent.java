package com.github.theword.queqiao.event.neoforge;

import com.github.theword.queqiao.tool.event.base.BasePlayerJoinEvent;

public final class NeoForgePlayerLoggedInEvent extends BasePlayerJoinEvent {
    public NeoForgePlayerLoggedInEvent(NeoForgeServerPlayer player) {
        super("NeoPlayerLoggedInEvent", player);
    }
}