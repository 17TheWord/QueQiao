package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.BasePlayerChatEvent;

public final class ForgeServerChatEvent extends BasePlayerChatEvent {
    public ForgeServerChatEvent(String messageId, ForgeServerPlayer player, String message) {
        super("ServerChatEvent", messageId, player, message);
    }

}