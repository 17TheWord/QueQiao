package com.github.theword.queqiao.event.spigot.dto.advancement;

import com.github.theword.queqiao.tool.event.base.BasePlayerAdvancementEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpigotAdvancement extends BasePlayerAdvancementEvent.BaseAdvancement {
    private Collection<String> criteria;
    // IF > spigot-1.12.2
//    /**
//     * Original: org.bukkit.advancement.AdvancementDisplay
//     * <p>version &gt 1.12.2</p>
//     */
//    private AdvancementDisplayDTO display;
    // END IF
}