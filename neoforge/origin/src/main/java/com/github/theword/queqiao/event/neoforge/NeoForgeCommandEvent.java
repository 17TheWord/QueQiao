package com.github.theword.queqiao.event.neoforge;

import com.github.theword.queqiao.tool.event.base.BaseCommandEvent;

public class NeoForgeCommandEvent extends BaseCommandEvent {
    public NeoForgeCommandEvent(String messageId, NeoForgeServerPlayer player, String command) {
        super("NeoCommandEvent", messageId, player, command);
    }
}