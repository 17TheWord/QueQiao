package com.github.theword.queqiao.event.neoforge;

import com.github.theword.queqiao.tool.event.base.BasePlayerChatEvent;

public final class NeoForgeServerChatEvent extends BasePlayerChatEvent {
    public NeoForgeServerChatEvent(String messageId, NeoForgeServerPlayer player, String message) {
        super("NeoServerChatEvent", messageId, player, message);
    }

}