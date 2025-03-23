package com.github.theword.queqiao.event.fabric.dto.advancement;

import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// IF <= fabric-1.20.1
//import com.google.gson.JsonElement;
// END IF

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FabricAdvancement extends BasePlayerAdvancementEvent.BaseAdvancement {

    private String id;

    /**
     * Original type: net.minecraft.util.Identifier
     * <p>toString()</p>
     */
    private String parent;

    /**
     * Original type: Optional &lt net.minecraft.advancement.net.minecraft.advancement.AdvancementDisplay &gt
     */

    private AdvancementDisplayDTO display;

    // IF <= fabric-1.20.1
//    /**
//     * Original type: JsonElement
//     * <p>toJson()</p>
//     * <p>version &lt 1.20.1</p>
//     */
//    private JsonElement rewards;
    // ELSE
//    /**
//     * Original type: net.minecraft.advancement.net.minecraft.advancement
//     * <p>version &gt 1.20.1</p>
//     */
//    private AdvancementRewardsDTO rewards;
    // END IF
    private Boolean sendsTelemetryEvent;

    /**
     * Original type: net.minecraft.text.Text
     * <p>getString()</p>
     */
    private String name;

}