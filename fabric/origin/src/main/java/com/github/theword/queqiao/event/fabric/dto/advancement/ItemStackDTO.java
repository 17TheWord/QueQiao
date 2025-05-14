package com.github.theword.queqiao.event.fabric.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStackDTO {
    private int count;

    /**
     * Original type: net.minecraft.text.Text
     *
     * <p>getString()</p>
     */
    private String name;
}