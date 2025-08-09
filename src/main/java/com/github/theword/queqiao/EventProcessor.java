package com.github.theword.queqiao;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.github.theword.queqiao.event.forge.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent()
            .isPlayerChat()) return;

        ForgeServerPlayer player = getForgePlayer(event.player);

        ForgeServerChatEvent forgeServerChatEvent = new ForgeServerChatEvent("", player, event.message);
        sendWebsocketMessage(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent()
            .isPlayerJoin()) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
        ForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new ForgePlayerLoggedInEvent(player);
        sendWebsocketMessage(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent()
            .isPlayerQuit()) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
        ForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new ForgePlayerLoggedOutEvent(player);
        sendWebsocketMessage(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent()
            .isPlayerCommand()) return;
        if (!(event.sender instanceof EntityPlayerMP)) return;

        String command = isRegisterOrLoginCommand(event.command.toString());
        if (command.isEmpty()) return;

        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.sender);
        ForgeCommandEvent forgeCommandEvent = new ForgeCommandEvent("", player, command);
        sendWebsocketMessage(forgeCommandEvent);

    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !config.getSubscribeEvent()
            .isPlayerDeath()) return;
        if (!(event.entityLiving instanceof EntityPlayerMP)) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.entityLiving);
        String reason = event.source.func_151519_b(event.entityLiving).getUnformattedText();
        ForgePlayerDeathEvent forgeCommandEvent = new ForgePlayerDeathEvent("", player, reason);
        sendWebsocketMessage(forgeCommandEvent);
    }
}
