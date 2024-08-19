package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public class FabricServerPlayConnectionDisconnectEvent extends BasePlayerQuitEvent {
    public FabricServerPlayConnectionDisconnectEvent(FabricServerPlayer player) {
        super("ServerPlayConnectionDisconnectEvent", player);
    }
}