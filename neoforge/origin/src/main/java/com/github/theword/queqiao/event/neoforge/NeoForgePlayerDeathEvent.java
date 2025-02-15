package com.github.theword.queqiao.event.neoforge;

import com.github.theword.queqiao.tool.event.base.BasePlayerDeathEvent;

public class NeoForgePlayerDeathEvent extends BasePlayerDeathEvent {

    public NeoForgePlayerDeathEvent(String messageId, NeoForgeServerPlayer player, String message) {
        super("NeoPlayerDeathEvent", messageId, player, message);
    }
}