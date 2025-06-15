package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.folia.FoliaPlayer;
import com.github.theword.queqiao.event.folia.dto.advancement.FoliaAdvancement;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import com.github.theword.queqiao.event.folia.dto.advancement.ItemMetaDTO;
import com.github.theword.queqiao.event.folia.dto.advancement.ItemStackDTO;
import com.github.theword.queqiao.event.folia.dto.advancement.AdvancementDisplayDTO;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FoliaTool {
    /**
     * 获取SpigotPlayer
     *
     * @param player 玩家
     * @return FoliaPlayer
     */
    public static FoliaPlayer getFoliaPlayer(Player player) {
        FoliaPlayer foliaPlayer = new FoliaPlayer();
        foliaPlayer.setUuid(player.getUniqueId());
        foliaPlayer.setNickname(player.getName());
        foliaPlayer.setDisplayName(player.displayName().examinableName());
        foliaPlayer.setPlayerListName(player.playerListName().examinableName());
        foliaPlayer.setAddress((Objects.requireNonNull(player.getAddress()).toString()));
        foliaPlayer.setHealthScale(player.getHealthScale());
        foliaPlayer.setExp(player.getExp());
        foliaPlayer.setTotalExp(player.getTotalExperience());
        foliaPlayer.setLevel(player.getLevel());
        foliaPlayer.setLocale(player.locale().toString());
        foliaPlayer.setPing(player.getPing());
        foliaPlayer.setPlayerTime(player.getPlayerTime());
        foliaPlayer.setPlayerTimeRelative(player.isPlayerTimeRelative());
        foliaPlayer.setPlayerTimeOffset(player.getPlayerTimeOffset());
        foliaPlayer.setWalkSpeed(player.getWalkSpeed());
        foliaPlayer.setFlySpeed(player.getFlySpeed());
        foliaPlayer.setAllowFlight(player.getAllowFlight());
        foliaPlayer.setSprinting(player.isSprinting());
        foliaPlayer.setSneaking(player.isSneaking());
        foliaPlayer.setFlying(player.isFlying());
        foliaPlayer.setOp(player.isOp());
        return foliaPlayer;
    }

    public static ItemMetaDTO getItemMetaDTO(ItemMeta itemMeta) {
        ItemMetaDTO itemMetaDTO = new ItemMetaDTO();
        if (itemMeta.hasCustomName()) itemMetaDTO.setCustomName(itemMeta.customName().toString());
        if (itemMeta.hasLore()) {
            List<String> collect = itemMeta.lore().stream().map(Component::toString).collect(Collectors.toList());
            itemMetaDTO.setLore(collect);
        }
        if (itemMeta.hasCustomModelData()) itemMetaDTO.setCustomModelData(itemMeta.getCustomModelData());
        if (itemMeta.hasEnchants()) itemMetaDTO.setEnchants(itemMeta.getEnchants());
        itemMetaDTO.setItemFlags(itemMeta.getItemFlags());
        itemMetaDTO.setUnbreakable(itemMeta.isUnbreakable());
        if (itemMeta.hasAttributeModifiers()) itemMetaDTO.setAttributeModifiers(itemMeta.getAttributeModifiers());
        return itemMetaDTO;
    }

    public static ItemStackDTO getItemStackDTO(ItemStack itemStack) {
        ItemStackDTO itemStackDTO = new ItemStackDTO();

        itemStackDTO.setAmount(itemStack.getAmount());
        itemStackDTO.setMaterial(itemStack.getType().name());

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            ItemMetaDTO itemMetaDTO = getItemMetaDTO(itemMeta);
            itemStackDTO.setMeta(itemMetaDTO);
        }
        return itemStackDTO;
    }


    public static FoliaAdvancement getFoliaAdvancement(Advancement advancement) {
        AdvancementDisplay advancementDisplay = advancement.getDisplay();

        AdvancementDisplayDTO advancementDisplayDTO;
        if (advancementDisplay != null) {
            ItemStackDTO itemStackDTO = getItemStackDTO(advancementDisplay.icon());
            advancementDisplayDTO = new AdvancementDisplayDTO();
            advancementDisplayDTO.setTitle(advancementDisplay.title().examinableName());
            advancementDisplayDTO.setDescription(advancementDisplay.description().examinableName());
            advancementDisplayDTO.setIcon(itemStackDTO);
            advancementDisplayDTO.setDoesShowToast(advancementDisplay.doesShowToast());
            advancementDisplayDTO.setDoesAnnounceToChat(advancementDisplay.doesAnnounceToChat());
            advancementDisplayDTO.setIsHidden(advancementDisplay.isHidden());
            advancementDisplayDTO.setFrame(advancementDisplay.frame());
        } else advancementDisplayDTO = null;

        FoliaAdvancement foliaAdvancement = new FoliaAdvancement(advancement.getCriteria(), advancementDisplayDTO);
        foliaAdvancement.setText(advancementDisplay != null ? advancementDisplay.title().examinableName() : "");
        return foliaAdvancement;
    }
}