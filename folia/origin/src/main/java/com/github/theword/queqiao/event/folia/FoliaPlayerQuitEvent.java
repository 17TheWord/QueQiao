package com.github.theword.queqiao.event.folia;

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent;

public class FoliaPlayerQuitEvent extends BasePlayerQuitEvent {

    public FoliaPlayerQuitEvent(FoliaPlayer player) {
        super("PlayerQuitEvent", player);
    }
}