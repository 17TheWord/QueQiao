package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.tool.event.BasePlayerDeathEvent;

public class FabricServerLivingEntityAfterDeathEvent extends BasePlayerDeathEvent {

    public FabricServerLivingEntityAfterDeathEvent(String messageId, FabricServerPlayer player, String message) {
        super("ServerLivingEntityAfterDeathEvent", messageId, player, message);
    }
}