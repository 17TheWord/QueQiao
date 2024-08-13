package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.base.BasePlayerChatEvent;

public class FabricServerMessageEvent extends BasePlayerChatEvent {
    public FabricServerMessageEvent(String messageId, FabricServerPlayer player, String message) {
        super("ServerMessageEvent", messageId, player, message);
    }
}