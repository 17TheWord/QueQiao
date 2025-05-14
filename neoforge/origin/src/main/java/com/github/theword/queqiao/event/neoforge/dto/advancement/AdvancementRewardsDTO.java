package com.github.theword.queqiao.event.neoforge.dto.advancement;

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
     * Original type: net.minecraft.resources.ResourceLocation
     *
     * <p>toString()</p>
     */
    private List<String> recipes;
}
