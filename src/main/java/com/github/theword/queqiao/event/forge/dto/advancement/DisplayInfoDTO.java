package com.github.theword.queqiao.event.forge.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayInfoDTO {
    /**
     * Original type: net.minecraft.network.chat.Component
     * <p>toString()</p>
     */
    private String title;

    /**
     * Original type: net.minecraft.network.chat.Component
     * <p>toString()</p>
     */
    private String description;
    private ItemStackDTO icon;

    /**
     * Original type: net.minecraft.util.ResourceLocation
     * <p>toString()</p>
     */
    private String background;
    /**
     * Original type: net.minecraft.advancements.FrameType
     * <p>getName()</p>
     */
    private String frame;
    private Boolean showToast;
    private Boolean announceToChat;
    private Boolean hidden;
    private Float x;
    private Float y;
}