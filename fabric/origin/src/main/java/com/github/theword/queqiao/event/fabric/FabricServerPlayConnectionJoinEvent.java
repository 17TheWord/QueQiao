package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.BasePlayerJoinEvent;

public class FabricServerPlayConnectionJoinEvent extends BasePlayerJoinEvent {
    public FabricServerPlayConnectionJoinEvent(FabricServerPlayer player) {
        super("ServerPlayConnectionJoinEvent", player);
    }
}