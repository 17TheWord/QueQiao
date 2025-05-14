package com.github.theword.queqiao.event.spigot.dto.advancement;

import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemMetaDTO {
    private String displayName;
    private String localizedName;
    private List<String> lore;
    private Integer customModelData;
    private Map<Enchantment, Integer> enchants;
    private Set<ItemFlag> itemFlags;
    private Boolean unbreakable;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
}