package com.github.theword.queqiao.event.spigot.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// IF > spigot-1.12.2
//import org.bukkit.advancement.AdvancementDisplayType;
// END IF
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancementDisplayDTO  {
    private String title;
    private String description;
    private ItemStackDTO icon;
    private Boolean shouldShowToast;
    private Boolean shouldAnnounceChat;
    private Boolean isHidden;
    private Float x;
    private Float y;
    // IF > spigot-1.12.2
//    private AdvancementDisplayType type;
    // END IF
}