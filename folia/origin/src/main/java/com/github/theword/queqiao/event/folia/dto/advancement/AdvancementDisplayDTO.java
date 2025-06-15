package com.github.theword.queqiao.event.folia.dto.advancement;

import io.papermc.paper.advancement.AdvancementDisplay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancementDisplayDTO  {
    private String title;
    private String description;
    private ItemStackDTO icon;
    private Boolean doesShowToast;
    private Boolean doesAnnounceToChat;
    private Boolean isHidden;
    private AdvancementDisplay.Frame  frame;
}