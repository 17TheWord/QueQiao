package com.github.theword.queqiao.event.folia.dto.advancement;

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
public class FoliaAdvancement extends BasePlayerAdvancementEvent.BaseAdvancement {
    private Collection<String> criteria;
    private AdvancementDisplayDTO display;
}