package com.github.theword.queqiao.event.minecraft;

import com.github.theword.queqiao.tool.event.base.BasePlayerChatEvent;

public class MinecraftPlayerChatEvent extends BasePlayerChatEvent {
    public MinecraftPlayerChatEvent(String messageId, MinecraftPlayer player, String message) {
        super("MinecraftPlayerChatEvent", messageId, player, message);
    }
}
