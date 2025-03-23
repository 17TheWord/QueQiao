package com.github.theword.queqiao.event.neoforge;

import com.github.theword.queqiao.event.neoforge.dto.advancement.NeoForgeAdvancement;
import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;

public class NeoForgeAdvancementEvent extends BasePlayerAdvancementEvent {
    public NeoForgeAdvancementEvent(NeoForgeServerPlayer player, NeoForgeAdvancement advancement) {
        super("NeoForgeAdvancementEvent", player, advancement);
    }
}