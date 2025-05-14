package com.github.theword.queqiao.event.forge.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStackDTO {
    private int count;
    private int popTime;
    /**
     * Original type: net.minecraft.item.Item
     *
     * <p>toString()</p>
     */
    private String item;

    /**
     * Original type: net.minecraft.network.chat.Component
     * <p>getString()</p>
     */
    private String displayName;
}