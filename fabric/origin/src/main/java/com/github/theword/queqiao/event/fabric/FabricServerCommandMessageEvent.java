package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.base.BaseCommandEvent;

public class FabricServerCommandMessageEvent extends BaseCommandEvent {
    public FabricServerCommandMessageEvent(String messageId, FabricServerPlayer player, String message) {
        super("ServerCommandMessageEvent", messageId, player, message);
    }
}