package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.event.forge.ForgeServerPlayer;
import com.github.theword.queqiao.event.forge.dto.advancement.AdvancementRewardsDTO;
import com.github.theword.queqiao.event.forge.dto.advancement.DisplayInfoDTO;
import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import com.github.theword.queqiao.event.forge.dto.advancement.ItemStackDTO;
import com.github.theword.queqiao.tool.GlobalContext;
import com.google.gson.JsonElement;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ForgeTool {
    public static ForgeServerPlayer getForgePlayer(EntityPlayerMP player) {
        ForgeServerPlayer forgeServerPlayer = new ForgeServerPlayer();

        forgeServerPlayer.setNickname(player.getName());
        forgeServerPlayer.setDisplayName(player.getDisplayNameString());

        forgeServerPlayer.setUuid(player.getGameProfile().getId());

        if(player.connection != null && player.connection.netManager != null){
            forgeServerPlayer.setIpAddress(player.getPlayerIP());
        }else {
            forgeServerPlayer.setIpAddress("");
        }

        forgeServerPlayer.setSpeed(player.capabilities.getWalkSpeed());
        forgeServerPlayer.setFlyingSpeed(player.capabilities.getFlySpeed());

        forgeServerPlayer.setGameMode(player.interactionManager.getGameType().getName());
        forgeServerPlayer.setFlying(player.capabilities.isFlying);
        forgeServerPlayer.setSleeping(player.isPlayerSleeping());
        forgeServerPlayer.setBlocking(player.isActiveItemStackBlocking());

        BlockPos position = player.getPosition();
        forgeServerPlayer.setBlockX(position.getX());
        forgeServerPlayer.setBlockY(position.getY());
        forgeServerPlayer.setBlockZ(position.getZ());

        return forgeServerPlayer;
    }
    public static ForgeAdvancement getForgeAdvancement(Advancement advancement) {
        ForgeAdvancement forgeAdvancement = new ForgeAdvancement();
        forgeAdvancement.setId(advancement.getId().toString());
        forgeAdvancement.setParent(advancement.getParent() != null ? advancement.getParent().getId().toString() : null);
        if (advancement.getDisplay() != null) {
            DisplayInfoDTO displayInfoDTO = new DisplayInfoDTO();
            displayInfoDTO.setTitle(advancement.getDisplay().getTitle().getFormattedText());
            displayInfoDTO.setDescription(advancement.getDisplay().getDescription().getFormattedText());
            // Set advancement icon
            ItemStack icon = advancement.getDisplay().icon;
            ItemStackDTO itemStackDTO = new ItemStackDTO();
            itemStackDTO.setCount(icon.getCount());
            itemStackDTO.setDisplayName(icon.getDisplayName());
            ResourceLocation itemID = icon.getItem().getRegistryName();
            if(itemID!=null)
                itemStackDTO.setItem(itemID.toString());
            displayInfoDTO.setIcon(itemStackDTO);
            forgeAdvancement.setDisplay(displayInfoDTO);
        }
        AdvancementRewardsDTO advancementRewardsDTO = new AdvancementRewardsDTO();
        advancementRewardsDTO.setExperience(advancement.getRewards().experience);

        advancementRewardsDTO.setLoot(Arrays.stream(advancement.getRewards().loot).map(ResourceLocation::toString).collect(Collectors.toList()));
        advancementRewardsDTO.setRecipes(Arrays.stream(advancement.getRewards().recipes).map(ResourceLocation::toString).collect(Collectors.toList()));
        forgeAdvancement.setRewards(advancementRewardsDTO);
        forgeAdvancement.setChatComponent(advancement.getDisplayText().getFormattedText());
        forgeAdvancement.setText(advancement.getDisplayText().getFormattedText());

        return forgeAdvancement;
    }

    /** 调用原版方法反序列化Json文本组件, 并将异常输出到日志
     * @param jsonElement 消息体
     * @return 文本组件, 或null如果解析出错
     */
    public static ITextComponent parseJsonToTextWrapped(JsonElement jsonElement){
        try {
            return ITextComponent.Serializer.fromJsonLenient(jsonElement.toString());
        } catch (Throwable e) {
            GlobalContext.getLogger().error("Error handling Broadcast Message {} : ",jsonElement,e);
        }
        return null;
    }
}