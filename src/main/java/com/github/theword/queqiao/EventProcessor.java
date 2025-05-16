package com.github.theword.queqiao;


import com.github.theword.queqiao.event.forge.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        logger.info(event.getPlayer().getDisplayNameString());
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerChat()) return;
        ForgeServerPlayer player = getForgePlayer(event.getPlayer());
        ForgeServerChatEvent forgeServerChatEvent = new ForgeServerChatEvent("", player, event.getMessage());
        sendWebsocketMessage(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        logger.info(event.toString());
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerJoin()) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
        ForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new ForgePlayerLoggedInEvent(player);
        sendWebsocketMessage(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        logger.info(event.toString());
        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerQuit()) return;
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
        ForgeServerPlayer player = getForgePlayer(entityPlayerMP);
        ForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new ForgePlayerLoggedOutEvent(player);
        sendWebsocketMessage(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
//        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerCommand()) return;
//        if (event.getSender().getCommandSenderEntity() == null || !(event.getSender().getCommandSenderEntity() instanceof EntityPlayerMP))
//            return;
//        String command = isRegisterOrLoginCommand(event.getCommand().toString());
//        if (command.isEmpty()) return;
//        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getSender().getCommandSenderEntity());
//        ForgeCommandEvent forgeCommandEvent = new ForgeCommandEvent("", player, command);
//        sendWebsocketMessage(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
//        if (event.isCanceled() || !config.getSubscribeEvent().isPlayerDeath()) return;
//        if (event.getEntity() == null || !(event.getEntity() instanceof EntityPlayerMP)) return;
//        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getEntity());
//        String message = event.getEntityLiving().getCombatTracker().getDeathMessage().getUnformattedText();
//        ForgePlayerDeathEvent forgeCommandEvent = new ForgePlayerDeathEvent("", player, message);
//        sendWebsocketMessage(forgeCommandEvent);
    }
}