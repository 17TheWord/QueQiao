package com.github.theword.queqiao.event.folia;

import com.github.theword.queqiao.event.folia.dto.advancement.FoliaAdvancement;
import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;

public class FoliaPlayerAdvancementDoneEvent extends BasePlayerAdvancementEvent {
    public FoliaPlayerAdvancementDoneEvent(FoliaPlayer player, FoliaAdvancement advancement) {
        super("PlayerAdvancementDoneEvent", player, advancement);
    }
}