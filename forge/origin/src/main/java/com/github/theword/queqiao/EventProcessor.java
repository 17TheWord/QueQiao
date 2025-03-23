package com.github.theword.queqiao;

import com.github.theword.queqiao.event.forge.*;
import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import com.github.theword.queqiao.tool.utils.GsonUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
// IF > forge-1.16.5
//import net.minecraft.advancements.Advancement;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
// ELSE
//import net.minecraft.advancements.Advancement;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
// END IF

import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAdvancement;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerChat()) return;

        ForgeServerPlayer player = getForgePlayer(event.getPlayer());

        // IF >= forge-1.19
//        String message = event.getMessage().getString();
        // ELSE
//        String message = event.getMessage();
        // END IF

        ForgeServerChatEvent forgeServerChatEvent = new ForgeServerChatEvent("", player, message);
        sendWebsocketMessage(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerJoin()) return;

        // IF > forge-1.16.5
//        ForgeServerPlayer player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        ForgeServerPlayer player = getForgePlayer((ServerPlayerEntity) event.getEntity());
        // END IF

        ForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new ForgePlayerLoggedInEvent(player);
        sendWebsocketMessage(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerQuit()) return;

        // IF > forge-1.16.5
//        ForgeServerPlayer player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        ForgeServerPlayer player = getForgePlayer((ServerPlayerEntity) event.getPlayer());
        // END IF

        ForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new ForgePlayerLoggedOutEvent(player);
        sendWebsocketMessage(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerCommand()) return;

        // IF >= forge-1.19
//        if (!event.getParseResults().getContext().getSource().isPlayer()) return;
        // ELSE IF >= forge-1.18
//        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayer)) return;
        // ELSE
//        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayerEntity)) return;
        // END IF

        String command = isRegisterOrLoginCommand(event.getParseResults().getReader().getString());

        if (command.isEmpty()) return;
        
        ForgeServerPlayer player;

        try {
            player = getForgePlayer(event.getParseResults().getContext().getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return;
        }
        ForgeCommandEvent forgeCommandEvent = new ForgeCommandEvent("", player, command);
        sendWebsocketMessage(forgeCommandEvent);

    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerDeath()) return;

        // IF > forge-1.16.5
//        if (!(event.getEntity() instanceof ServerPlayer)) return;
//        ForgeServerPlayer player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        if (!(event.getEntity() instanceof ServerPlayerEntity)) return;
//        ForgeServerPlayer player = getForgePlayer((ServerPlayerEntity) event.getEntity());
        // END IF

        // IF >= forge-1.19
//        LivingEntity entity = event.getEntity();
        // ELSE
//        LivingEntity entity = (LivingEntity) event.getEntity();
        // END IF

        String message = event.getSource().getLocalizedDeathMessage(entity).getString();

        ForgePlayerDeathEvent forgeCommandEvent = new ForgePlayerDeathEvent("", player, message);
        sendWebsocketMessage(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent event) {
        if (!config.getSubscribeEvent().isPlayerAdvancement()) return;
        // IF <= forge-1.20.1
//        Advancement advancement = event.getAdvancement();
        // ELSE
//        Advancement advancement = event.getAdvancement().value();
        // END IF
        
        // IF > forge-1.16.5
//        ForgeServerPlayer player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        ForgeServerPlayer player = getForgePlayer((ServerPlayerEntity) event.getPlayer());
        // END IF

        ForgeAdvancement forgeAdvancement = getForgeAdvancement(advancement);

        ForgeAdvancementEvent forgeAdvancementEvent = new ForgeAdvancementEvent(player, forgeAdvancement);
        sendWebsocketMessage(forgeAdvancementEvent);
    }
}