package com.github.theword.queqiao.event.forge;

import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;

public class ForgeAdvancementEvent extends BasePlayerAdvancementEvent {
    public ForgeAdvancementEvent(ForgeServerPlayer player, ForgeAdvancement advancement) {
        super("ForgeAdvancementEvent", player, advancement);
    }
}
