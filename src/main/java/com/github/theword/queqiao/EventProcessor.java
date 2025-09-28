package com.github.theword.queqiao;

import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

import com.github.theword.queqiao.tool.GlobalContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.github.theword.queqiao.event.forge.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        ForgeServerPlayer player = getForgePlayer(event.player);

        ForgeServerChatEvent forgeServerChatEvent = new ForgeServerChatEvent("", player, event.message);
        GlobalContext.getWebsocketManager().sendEvent(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
        ForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new ForgePlayerLoggedInEvent(player);
        GlobalContext.getWebsocketManager().sendEvent(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
        ForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new ForgePlayerLoggedOutEvent(player);
        GlobalContext.getWebsocketManager().sendEvent(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;
        if (!(event.sender instanceof EntityPlayerMP)) return;

        String command = isRegisterOrLoginCommand(event.command.toString());
        if (command.isEmpty()) return;

        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.sender);
        ForgeCommandEvent forgeCommandEvent = new ForgeCommandEvent("", player, command);
        GlobalContext.getWebsocketManager().sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
        if (!(event.entityLiving instanceof EntityPlayerMP)) return;
        ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.entityLiving);
        String reason = event.source.func_151519_b(event.entityLiving).getUnformattedText();
        ForgePlayerDeathEvent forgeCommandEvent = new ForgePlayerDeathEvent("", player, reason);
        GlobalContext.getWebsocketManager().sendEvent(forgeCommandEvent);
    }

}
