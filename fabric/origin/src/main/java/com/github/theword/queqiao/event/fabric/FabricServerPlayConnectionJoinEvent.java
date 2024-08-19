package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.base.BasePlayerJoinEvent;

public class FabricServerPlayConnectionJoinEvent extends BasePlayerJoinEvent {
    public FabricServerPlayConnectionJoinEvent(FabricServerPlayer player) {
        super("ServerPlayConnectionJoinEvent", player);
    }
}