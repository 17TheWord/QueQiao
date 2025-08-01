package com.github.theword.queqiao.event.folia;

import com.github.theword.queqiao.tool.event.base.BasePlayerChatEvent;

public class FoliaAsyncPlayerChatEvent extends BasePlayerChatEvent {

    public FoliaAsyncPlayerChatEvent(FoliaPlayer player, String message) {
        super("FoliaAsyncPlayerChatEvent", "", player, message);
    }
}