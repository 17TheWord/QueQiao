package com.github.theword.queqiao.event.neoforge.dto.advancement;

import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NeoForgeAdvancement extends BasePlayerAdvancementEvent.BaseAdvancement {
    /**
     * Original type: net.minecraft.resources.ResourceLocation
     * <p>toString()</p>
     */
    private String parent;

    private DisplayInfoDTO display;

    /**
     * Original type: net.minecraft.advancements.AdvancementRewards
     * <p>serializeToJson()</p>
     */
    private AdvancementRewardsDTO rewards;

    /**
     * Original type: net.minecraft.util.ResourceLocation
     * <p>toString()</p>
     */
    private String name;

    private Boolean sendsTelemetryEvent;
}
