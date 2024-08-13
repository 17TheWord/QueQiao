package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.BaseCommandEvent;

public class ForgeCommandEvent extends BaseCommandEvent {
    public ForgeCommandEvent(String messageId, ForgeServerPlayer player, String command) {
        super("CommandEvent", messageId, player, command);
    }
}