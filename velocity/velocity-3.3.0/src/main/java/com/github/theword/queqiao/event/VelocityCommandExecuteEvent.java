package com.github.theword.queqiao.event;

import com.github.theword.queqiao.tool.event.base.BaseCommandEvent;

public class VelocityCommandExecuteEvent extends BaseCommandEvent {
    public VelocityCommandExecuteEvent(VelocityPlayer player, String command) {
        super("VelocityCommandExecuteEvent", "", player, command);
    }
}
