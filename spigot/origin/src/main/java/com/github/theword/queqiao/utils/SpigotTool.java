package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.spigot.SpigotPlayer;
import com.github.theword.queqiao.event.spigot.dto.advancement.SpigotAdvancement;
import com.google.gson.JsonElement;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

// IF > spigot-1.12.2
//import com.github.theword.queqiao.event.spigot.dto.advancement.ItemMetaDTO;
//import com.github.theword.queqiao.event.spigot.dto.advancement.ItemStackDTO;
//import com.github.theword.queqiao.event.spigot.dto.advancement.AdvancementDisplayDTO;
//import org.bukkit.advancement.AdvancementDisplay;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
// END IF

import java.util.Objects;

public class SpigotTool {
    /**
     * 获取SpigotPlayer
     *
     * @param player 玩家
     * @return SpigotPlayer
     */
    public static SpigotPlayer getSpigotPlayer(Player player) {
        SpigotPlayer spigotPlayer = new SpigotPlayer();
        spigotPlayer.setUuid(player.getUniqueId());
        spigotPlayer.setNickname(player.getName());
        spigotPlayer.setDisplayName(player.getDisplayName());
        spigotPlayer.setPlayerListName(player.getDisplayName());
        spigotPlayer.setAddress((Objects.requireNonNull(player.getAddress()).toString()));
        spigotPlayer.setHealthScale(player.getHealthScale());
        spigotPlayer.setExp(player.getExp());
        spigotPlayer.setTotalExp(player.getTotalExperience());
        spigotPlayer.setLevel(player.getLevel());
        spigotPlayer.setLocale(player.getLocale());
        // IF spigot-1.12.2
//        spigotPlayer.setPing(-1);
        // ELSE
//        spigotPlayer.setPing(player.getPing());
        // END IF
        spigotPlayer.setPlayerTime(player.getPlayerTime());
        spigotPlayer.setPlayerTimeRelative(player.isPlayerTimeRelative());
        spigotPlayer.setPlayerTimeOffset(player.getPlayerTimeOffset());
        spigotPlayer.setWalkSpeed(player.getWalkSpeed());
        spigotPlayer.setFlySpeed(player.getFlySpeed());
        spigotPlayer.setAllowFlight(player.getAllowFlight());
        spigotPlayer.setSprinting(player.isSprinting());
        spigotPlayer.setSneaking(player.isSneaking());
        spigotPlayer.setFlying(player.isFlying());
        spigotPlayer.setOp(player.isOp());
        return spigotPlayer;
    }

    // IF > spigot-1.12.2
//    public static ItemMetaDTO getItemMetaDTO(ItemMeta itemMeta) {
//        ItemMetaDTO itemMetaDTO = new ItemMetaDTO();
//        if (itemMeta.hasDisplayName()) itemMetaDTO.setDisplayName(itemMeta.getDisplayName());
//        if (itemMeta.hasLocalizedName()) itemMetaDTO.setLocalizedName(itemMeta.getLocalizedName());
//        if (itemMeta.hasLore()) itemMetaDTO.setLore(itemMeta.getLore());
//        if (itemMeta.hasCustomModelData()) itemMetaDTO.setCustomModelData(itemMeta.getCustomModelData());
//        if (itemMeta.hasEnchants()) itemMetaDTO.setEnchants(itemMeta.getEnchants());
//        itemMetaDTO.setItemFlags(itemMeta.getItemFlags());
//        itemMetaDTO.setUnbreakable(itemMeta.isUnbreakable());
//        if (itemMeta.hasAttributeModifiers()) itemMetaDTO.setAttributeModifiers(itemMeta.getAttributeModifiers());
//        return itemMetaDTO;
//    }
//
//    public static ItemStackDTO getItemStackDTO(ItemStack itemStack) {
//        ItemStackDTO itemStackDTO = new ItemStackDTO();
//
//        itemStackDTO.setAmount(itemStack.getAmount());
//        itemStackDTO.setMaterial(itemStack.getType().name());
//
//        ItemMeta itemMeta = itemStack.getItemMeta();
//        if (itemMeta != null) {
//            ItemMetaDTO itemMetaDTO = getItemMetaDTO(itemMeta);
//            itemStackDTO.setMeta(itemMetaDTO);
//        }
//        return itemStackDTO;
//    }
//
    // END IF

    public static SpigotAdvancement getSpigotAdvancement(Advancement advancement) {
        // IF spigot-1.12.2
//        return new SpigotAdvancement(advancement.getCriteria());
        // ELSE
//        AdvancementDisplay advancementDisplay = advancement.getDisplay();
//
//        AdvancementDisplayDTO advancementDisplayDTO;
//        if (advancementDisplay != null) {
//            ItemStackDTO itemStackDTO = getItemStackDTO(advancementDisplay.getIcon());
//            advancementDisplayDTO = new AdvancementDisplayDTO();
//            advancementDisplayDTO.setTitle(advancementDisplay.getTitle());
//            advancementDisplayDTO.setDescription(advancementDisplay.getDescription());
//            advancementDisplayDTO.setIcon(itemStackDTO);
//            advancementDisplayDTO.setShouldShowToast(advancementDisplay.shouldShowToast());
//            advancementDisplayDTO.setShouldAnnounceChat(advancementDisplay.shouldAnnounceChat());
//            advancementDisplayDTO.setIsHidden(advancementDisplay.isHidden());
//            advancementDisplayDTO.setX(advancementDisplay.getX());
//            advancementDisplayDTO.setY(advancementDisplay.getY());
//            advancementDisplayDTO.setType(advancementDisplay.getType());
//        } else advancementDisplayDTO = null;
//
//        SpigotAdvancement spigotAdvancement = new SpigotAdvancement(advancement.getCriteria(), advancementDisplayDTO);
//        spigotAdvancement.setText(advancementDisplay != null ? advancementDisplay.getTitle() : "");
//        return spigotAdvancement;
        // END IF
    }

    public static BaseComponent[] buildComponent(JsonElement jsonElement) {
        return ComponentSerializer.parse(jsonElement.toString());
    }
}