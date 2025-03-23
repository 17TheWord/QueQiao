package com.github.theword.queqiao.event.forge.dto.advancement;

import com.github.theword.queqiao.event.fabric.dto.advancement.AdvancementRewardsDTO;
import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// IF < forge-1.21
//import com.google.gson.JsonElement;
// END IF

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ForgeAdvancement extends BasePlayerAdvancementEvent.BaseAdvancement {

    /**
     * Original type: net.minecraft.advancements.Advancement
     * <p>getId().toString()</p>
     */
    private String parent;

    private DisplayInfoDTO display;

    // IF < forge-1.21
//    /**
//     * Original type: net.minecraft.util.ResourceLocation
//     * <p>toString()</p>
//     * <p>version &lt 1.21</p>
//     */
//    private String id;
//    /**
//     * Original type: net.minecraft.advancements.AdvancementRewards
//     * <p>serializeToJson()</p>
//     * <p>version &lt 1.21</p>
//     */
//    private JsonElement rewards;
//    /**
//     * Original type: net.minecraft.network.chat.Component
//     * <p>toString()</p>
//     * <p>version &lt 1.21</p>
//     */
//    private String chatComponent;
    // ELSE
//    /**
//     * Original type: net.minecraft.network.chat.Component
//     * <p>getString()</p>
//     * <p>version &gt 1.21</p>
//     */
//    private String name;
//    /**
//     * Original type: net.minecraft.advancements.AdvancementRewards
//     * <p>serializeToJson()</p>
//     * <p>version &gt 1.21</p>
//     */
//    private AdvancementRewardsDTO rewards;
    // END IF


    private Boolean sendsTelemetryEvent;
}