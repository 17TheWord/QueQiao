package com.github.theword.queqiao.event.folia;

import com.github.theword.queqiao.tool.event.base.BasePlayerDeathEvent;

public class FoliaPlayerDeathEvent extends BasePlayerDeathEvent {

    public FoliaPlayerDeathEvent(FoliaPlayer player, String message) {
        super("PlayerDeathEvent", "", player, message);
    }
}