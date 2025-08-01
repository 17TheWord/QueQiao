package com.github.theword.queqiao.event.folia;

import com.github.theword.queqiao.tool.event.base.BaseCommandEvent;

public class FoliaPlayerCommandPreprocessEvent extends BaseCommandEvent {

    public FoliaPlayerCommandPreprocessEvent(FoliaPlayer player, String command) {
        super("FoliaPlayerCommandPreprocessEvent", "", player, command);
    }
}