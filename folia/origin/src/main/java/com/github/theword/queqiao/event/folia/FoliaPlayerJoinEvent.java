package com.github.theword.queqiao.event.folia;

import com.github.theword.queqiao.tool.event.base.BasePlayerJoinEvent;

public class FoliaPlayerJoinEvent extends BasePlayerJoinEvent {

    public FoliaPlayerJoinEvent(FoliaPlayer player) {
        super("PlayerJoinEvent", player);
    }

}