package com.github.theword.queqiao;


import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.*;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Arrays;

import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAchievement;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        try {
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;
            PlayerModel player = getForgePlayer(event.getPlayer());
            ITextComponent.Serializer.componentToJson(event.getComponent());
            PlayerChatEvent forgeServerChatEvent = new PlayerChatEvent(player, "", ITextComponent.Serializer.componentToJson(event.getComponent()), event.getMessage());
            GlobalContext.sendEvent(forgeServerChatEvent);
        } catch (Exception e) {
            GlobalContext.getLogger().error("Error processing ServerChatEvent: ", e);
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;
            PlayerModel player = getForgePlayer((EntityPlayerMP) event.player);
            PlayerJoinEvent forgePlayerLoggedInEvent = new PlayerJoinEvent(player);
            GlobalContext.sendEvent(forgePlayerLoggedInEvent);
        } catch (Exception e) {
            GlobalContext.getLogger().error("Error processing PlayerLoggedInEvent: ", e);
        }
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        try {
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
            PlayerModel player = getForgePlayer(entityPlayerMP);
            PlayerQuitEvent forgePlayerLoggedOutEvent = new PlayerQuitEvent(player);
            GlobalContext.sendEvent(forgePlayerLoggedOutEvent);
        } catch (Exception e) {
            GlobalContext.getLogger().error("Error processing PlayerLoggedOutEvent: ", e);
        }
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        try {
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;
            if (!(event.getSender() instanceof EntityPlayerMP)) return;

            StringBuilder commandString = new StringBuilder(event.getCommand().getName());
            for (String parameter : event.getParameters()) {
                commandString.append(" ").append(parameter);
            }
            String command = isRegisterOrLoginCommand(commandString.toString());
            if (command.isEmpty()) return;

            PlayerModel player = getForgePlayer((EntityPlayerMP) event.getSender());
            PlayerCommandEvent forgeCommandEvent = new PlayerCommandEvent(player, "", command, command);
            GlobalContext.sendEvent(forgeCommandEvent);
        } catch (Exception e) {
            GlobalContext.getLogger().error("Error processing CommandEvent: ", e);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        try {
            if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
            if (event.getEntity() == null || !(event.getEntity() instanceof EntityPlayerMP)) return;
            PlayerModel player = getForgePlayer((EntityPlayerMP) event.getEntity());
            ITextComponent deathMessage = event.getEntityLiving().getCombatTracker().getDeathMessage();

            DeathModel deathModel = new DeathModel();
            deathModel.setText(deathMessage.getUnformattedText());

            TextComponentTranslation deathMessageTranslation = (TextComponentTranslation) deathMessage;

            deathModel.setKey(deathMessageTranslation.getKey());

            String[] args = Arrays.stream(deathMessageTranslation.getFormatArgs()).map(obj -> {
                if (obj instanceof ITextComponent) {
                    return ((ITextComponent) obj).getUnformattedText();
                } else {
                    return String.valueOf(obj);
                }
            }).toArray(String[]::new);
            deathModel.setArgs(args);

            PlayerDeathEvent forgePlayerDeathEvent = new PlayerDeathEvent(player, deathModel);
            GlobalContext.sendEvent(forgePlayerDeathEvent);
        } catch (Exception e) {
            GlobalContext.getLogger().error("Error processing LivingDeathEvent: ", e);
        }
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent event) {
        try {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
            Advancement advancement = event.getAdvancement();
            if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceToChat()) return;

            PlayerModel player = getForgePlayer((EntityPlayerMP) event.getEntityPlayer());

            AchievementModel achievementModel = getForgeAchievement(advancement);
            achievementModel.pattern(achievementModel.getDisplay().getFrame(), player.getNickname(), advancement.getDisplayText().getUnformattedText());

            PlayerAchievementEvent forgeAdvancementEvent = new PlayerAchievementEvent(player, achievementModel);
            GlobalContext.sendEvent(forgeAdvancementEvent);
        } catch (Exception e) {
            GlobalContext.getLogger().error("Error processing AdvancementEvent: ", e);
        }
    }
}