package com.github.theword.queqiao;


import com.github.theword.queqiao.event.forge.*;
import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAdvancement;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerChat()) return;
        ForgeServerPlayer player = getForgePlayer(event.getPlayer());
        ForgeServerChatEvent forgeServerChatEvent = new ForgeServerChatEvent("", player, event.getMessage());
        sendWebsocketMessage(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerJoin()) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
        ForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new ForgePlayerLoggedInEvent(player);
        sendWebsocketMessage(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerQuit()) return;
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
        ForgeServerPlayer player = getForgePlayer(entityPlayerMP);
        ForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new ForgePlayerLoggedOutEvent(player);
        sendWebsocketMessage(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerCommand()) return;
        if(!(event.getSender() instanceof EntityPlayerMP)) return;

        String command = isRegisterOrLoginCommand(event.getCommand().toString());
        if (command.isEmpty()) return;

        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getSender());
        ForgeCommandEvent forgeCommandEvent = new ForgeCommandEvent("", player, command);
        sendWebsocketMessage(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerDeath()) return;
        if (event.getEntity() == null || !(event.getEntity() instanceof EntityPlayerMP)) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getEntity());
        String message = event.getEntityLiving().getCombatTracker().getDeathMessage().getUnformattedText();
        ForgePlayerDeathEvent forgePlayerDeathEvent = new ForgePlayerDeathEvent("", player, message);
        sendWebsocketMessage(forgePlayerDeathEvent);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent event) {
        if (!config.getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement();

        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getEntityPlayer());

        ForgeAdvancement forgeAdvancement = getForgeAdvancement(advancement);

        ForgeAdvancementEvent forgeAdvancementEvent = new ForgeAdvancementEvent(player, forgeAdvancement);
        sendWebsocketMessage(forgeAdvancementEvent);
    }
}