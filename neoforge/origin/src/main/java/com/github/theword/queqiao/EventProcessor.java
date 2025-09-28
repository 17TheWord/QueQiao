package com.github.theword.queqiao;

import com.github.theword.queqiao.event.neoforge.*;
import com.github.theword.queqiao.event.neoforge.dto.advancement.NeoForgeAdvancement;
import com.github.theword.queqiao.tool.GlobalContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
import static com.github.theword.queqiao.utils.NeoForgeTool.getNeoForgeAdvancement;
import static com.github.theword.queqiao.utils.NeoForgeTool.getNeoForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        NeoForgeServerPlayer player = getNeoForgePlayer(event.getPlayer());

        String message = event.getMessage().getString();

        NeoForgeServerChatEvent NeoForgeServerChatEvent = new NeoForgeServerChatEvent("", player, message);
        GlobalContext.getWebsocketManager().sendEvent(NeoForgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        NeoForgeServerPlayer player = getNeoForgePlayer((ServerPlayer) event.getEntity());

        NeoForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new NeoForgePlayerLoggedInEvent(player);
        GlobalContext.getWebsocketManager().sendEvent(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        NeoForgeServerPlayer player = getNeoForgePlayer((ServerPlayer) event.getEntity());

        NeoForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new NeoForgePlayerLoggedOutEvent(player);
        GlobalContext.getWebsocketManager().sendEvent(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        if (!event.getParseResults().getContext().getSource().isPlayer()) return;

        String command = isRegisterOrLoginCommand(event.getParseResults().getReader().getString());

        if (command.isEmpty()) return;

        NeoForgeServerPlayer player;

        try {
            player = getNeoForgePlayer(event.getParseResults().getContext().getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return;
        }
        NeoForgeCommandEvent forgeCommandEvent = new NeoForgeCommandEvent("", player, command);
        GlobalContext.getWebsocketManager().sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        if (!(event.getEntity() instanceof ServerPlayer)) return;
        NeoForgeServerPlayer player = getNeoForgePlayer((ServerPlayer) event.getEntity());

        LivingEntity entity = event.getEntity();

        String message = event.getSource().getLocalizedDeathMessage(entity).getString();

        NeoForgePlayerDeathEvent forgeCommandEvent = new NeoForgePlayerDeathEvent("", player, message);
        GlobalContext.getWebsocketManager().sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement().value();
        NeoForgeAdvancement neoForgeAdvancement = getNeoForgeAdvancement(advancement);
        NeoForgeAdvancementEvent forgeAdvancementEvent = new NeoForgeAdvancementEvent(getNeoForgePlayer((ServerPlayer) event.getEntity()), neoForgeAdvancement);
        GlobalContext.getWebsocketManager().sendEvent(forgeAdvancementEvent);
    }
}