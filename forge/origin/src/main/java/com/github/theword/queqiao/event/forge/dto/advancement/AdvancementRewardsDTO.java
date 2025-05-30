package com.github.theword.queqiao.event.fabric.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancementRewardsDTO {

    private Integer experience;

    /**
     * Original type: net.minecraft.world.level.storage.loot.LootTable
     *
     * <p>getLootTableId().toString()</p>
     */
    private List<String> loot;

    /**
     * Original type: List &lt;net.minecraft.resources.Identifier&gt;
     *
     * <p>toString()</p>
     */
    private List<String> recipes;
}