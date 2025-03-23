package com.github.theword.queqiao.event.spigot;

import com.github.theword.queqiao.event.spigot.dto.advancement.SpigotAdvancement;
import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;

public class SpigotPlayerAdvancementDoneEvent extends BasePlayerAdvancementEvent {
    public SpigotPlayerAdvancementDoneEvent(SpigotPlayer player, SpigotAdvancement advancement) {
        super("PlayerAdvancementDoneEvent", player, advancement);
    }
}