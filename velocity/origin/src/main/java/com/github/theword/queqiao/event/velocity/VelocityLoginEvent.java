package com.github.theword.queqiao.event.velocity;

import com.github.theword.queqiao.tool.event.base.BasePlayerJoinEvent;

public class VelocityLoginEvent extends BasePlayerJoinEvent {
    public VelocityLoginEvent(VelocityPlayer player) {
        super("VelocityLoginEvent", player);
    }
}
