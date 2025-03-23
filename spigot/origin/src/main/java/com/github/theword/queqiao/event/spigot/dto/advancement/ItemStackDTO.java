package com.github.theword.queqiao.event.spigot.dto.advancement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStackDTO {
    private String material;
    private ItemMetaDTO meta;
    private Integer amount;
}