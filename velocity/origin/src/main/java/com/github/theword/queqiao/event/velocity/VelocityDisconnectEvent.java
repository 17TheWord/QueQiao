package com.github.theword.queqiao.event.velocity;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public class VelocityDisconnectEvent extends BasePlayerQuitEvent {
    public VelocityDisconnectEvent(VelocityPlayer player) {
        super("VelocityDisconnectEvent", player);
    }
}
