package com.github.theword.queqiao.event.spigot;

import com.github.theword.queqiao.tool.event.BasePlayerChatEvent;

public class SpigotAsyncPlayerChatEvent extends BasePlayerChatEvent {

    public SpigotAsyncPlayerChatEvent(SpigotPlayer player, String message) {
        super("AsyncPlayerChatEvent", "", player, message);
    }
}