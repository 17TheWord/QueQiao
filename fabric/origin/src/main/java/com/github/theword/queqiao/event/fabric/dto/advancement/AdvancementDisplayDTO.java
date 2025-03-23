package com.github.theword.queqiao.event.fabric.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancementDisplayDTO {
    /**
     * Original type: net.minecraft.text.Text
     * <p>getString()</p>
     */
    private String title;

    /**
     * Original type: net.minecraft.text.Text
     * <p>getString()</p>
     */
    private String description;

    private ItemStackDTO icon;

    /**
     * Original type: Optional &lt;net.minecraft.util.Identifier&gt;
     * <p>toString()</p>
     */
    private String background;

    /**
     * Original type: net.minecraft.advancements.AdvancementFrame
     * <p>getToastText().toString()</p>
     */
    private String frame;

    private Boolean showToast;

    private Boolean announceToChat;

    private Boolean hidden;

    private Float x;

    private Float y;
}
