package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.tool.event.BasePlayerDeathEvent;

public class ForgePlayerDeathEvent extends BasePlayerDeathEvent {

    public ForgePlayerDeathEvent(String messageId, ForgeServerPlayer player, String message) {
        super("PlayerDeathEvent", messageId, player, message);
    }
}