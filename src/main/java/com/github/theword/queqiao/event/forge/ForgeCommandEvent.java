package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.base.BaseCommandEvent;

public class ForgeCommandEvent extends BaseCommandEvent {
    public ForgeCommandEvent(String messageId, ForgeServerPlayer player, String command) {
        super("CommandEvent", messageId, player, command);
    }
}