package com.github.theword.queqiao.event.fabric;

import com.github.theword.queqiao.event.fabric.dto.advancement.FabricAdvancement;
import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;

public class FabricAdvancementCriterionEvent extends BasePlayerAdvancementEvent {

    public FabricAdvancementCriterionEvent(FabricServerPlayer player, FabricAdvancement advancement) {
        super("FabricAdvancementCriterionEvent", player, advancement);
    }
}