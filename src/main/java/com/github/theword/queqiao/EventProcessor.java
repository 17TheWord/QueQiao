package com.github.theword.queqiao;


import com.github.theword.queqiao.event.forge.*;
import com.github.theword.queqiao.event.forge.dto.advancement.ForgeAdvancement;
import com.github.theword.queqiao.tool.GlobalContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAdvancement;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        try {
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;
            ForgeServerPlayer player = getForgePlayer(event.getPlayer());
            ForgeServerChatEvent forgeServerChatEvent = new ForgeServerChatEvent("", player, event.getMessage());
            GlobalContext.getWebsocketManager().sendEvent(forgeServerChatEvent);
        }catch (Exception e){
            GlobalContext.getLogger().error("Error processing ServerChatEvent: ",e);
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        try{
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;
            ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.player);
            ForgePlayerLoggedInEvent forgePlayerLoggedInEvent = new ForgePlayerLoggedInEvent(player);
            GlobalContext.getWebsocketManager().sendEvent(forgePlayerLoggedInEvent);
        }catch (Exception e){
            GlobalContext.getLogger().error("Error processing PlayerLoggedInEvent: ",e);
        }
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        try{
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
            ForgeServerPlayer player = getForgePlayer(entityPlayerMP);
            ForgePlayerLoggedOutEvent forgePlayerLoggedOutEvent = new ForgePlayerLoggedOutEvent(player);
            GlobalContext.getWebsocketManager().sendEvent(forgePlayerLoggedOutEvent);
        }catch (Exception e){
            GlobalContext.getLogger().error("Error processing PlayerLoggedOutEvent: ",e);
        }
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        try{
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;
            if(!(event.getSender() instanceof EntityPlayerMP)) return;

            StringBuilder commandString = new StringBuilder(event.getCommand().getName());
            for (String parameter:event.getParameters()){
                commandString.append(" ").append(parameter);
            }
            String command = isRegisterOrLoginCommand(commandString.toString());
            if (command.isEmpty()) return;

            ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getSender());
            ForgeCommandEvent forgeCommandEvent = new ForgeCommandEvent("", player, command);
            GlobalContext.getWebsocketManager().sendEvent(forgeCommandEvent);
        }catch (Exception e){
            GlobalContext.getLogger().error("Error processing CommandEvent: ",e);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        try{
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
            if (event.getEntity() == null || !(event.getEntity() instanceof EntityPlayerMP)) return;
            ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getEntity());
            String message = event.getEntityLiving().getCombatTracker().getDeathMessage().getUnformattedText();
            ForgePlayerDeathEvent forgePlayerDeathEvent = new ForgePlayerDeathEvent("", player, message);
            GlobalContext.getWebsocketManager().sendEvent(forgePlayerDeathEvent);
        }catch (Exception e){
            GlobalContext.getLogger().error("Error processing LivingDeathEvent: ",e);
        }
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent event) {
        try{
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
            Advancement advancement = event.getAdvancement();

            ForgeServerPlayer player = getForgePlayer((EntityPlayerMP) event.getEntityPlayer());

            ForgeAdvancement forgeAdvancement = getForgeAdvancement(advancement);

            ForgeAdvancementEvent forgeAdvancementEvent = new ForgeAdvancementEvent(player, forgeAdvancement);
            GlobalContext.getWebsocketManager().sendEvent(forgeAdvancementEvent);
        }catch (Exception e){
            GlobalContext.getLogger().error("Error processing AdvancementEvent: ",e);
        }
    }
}