package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.*;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.advancements.Advancement;

// IF forge-1.16.5
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
// ELSE
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.LivingEntity;
// END IF

// IF forge-1.18.2
//import net.minecraft.network.chat.TranslatableComponent;
// ELSE IF >= forge-1.19
//import net.minecraft.network.chat.contents.TranslatableContents;
// END IF

import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

// IF >= forge-1.21
//import static com.github.theword.queqiao.QueQiao.minecraftServer;
// END IF
import java.util.Arrays;

import static com.github.theword.queqiao.tool.utils.Tool.*;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAchievement;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        PlayerModel player = getForgePlayer(event.getPlayer());
        // IF forge-1.16.5
//        String message = event.getMessage();
//        String rawMessage = ITextComponent.Serializer.toJson(event.getComponent());
        // ELSE IF forge-1.18.2
//        String message = event.getMessage();
//        String rawMessage = Component.Serializer.toJson(event.getComponent());
        // ELSE IF < forge-1.21
//        String message = event.getMessage().getString();
//        String rawMessage = Component.Serializer.toJson(event.getMessage());
        // ELSE
//        String message = event.getMessage().getString();
//        String rawMessage = Component.Serializer.toJson(event.getMessage(), minecraftServer.registryAccess());
        // END IF

        PlayerChatEvent forgeServerChatEvent = new PlayerChatEvent(player, "", rawMessage, message);
        GlobalContext.sendEvent(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        // IF > forge-1.16.5
//        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        PlayerModel player = getForgePlayer((ServerPlayerEntity) event.getEntity());
        // END IF

        PlayerJoinEvent forgePlayerLoggedInEvent = new PlayerJoinEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        // IF > forge-1.16.5
//        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        PlayerModel player = getForgePlayer((ServerPlayerEntity) event.getPlayer());
        // END IF

        PlayerQuitEvent forgePlayerLoggedOutEvent = new PlayerQuitEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        // IF >= forge-1.19
//        if (!event.getParseResults().getContext().getSource().isPlayer()) return;
        // ELSE IF >= forge-1.18
//        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayer)) return;
        // ELSE
//        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayerEntity)) return;
        // END IF

        String command = isRegisterOrLoginCommand(event.getParseResults().getReader().getString());
        String rawMessage = event.getParseResults().getContext().toString();

        if (command.isEmpty()) return;

        PlayerModel player;

        try {
            player = getForgePlayer(event.getParseResults().getContext().getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return;
        }
        PlayerCommandEvent forgeCommandEvent = new PlayerCommandEvent(player, "", rawMessage, command);
        GlobalContext.sendEvent(forgeCommandEvent);

    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        DeathModel deathModel = new DeathModel();
        // IF > forge-1.16.5
//        if (!(event.getEntity() instanceof ServerPlayer)) return;
//        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        if (!(event.getEntity() instanceof ServerPlayerEntity)) return;
//        PlayerModel player = getForgePlayer((ServerPlayerEntity) event.getEntity());
        // END IF

        // IF >= forge-1.19
//        LivingEntity entity = event.getEntity();
        // ELSE
//        LivingEntity entity = (LivingEntity) event.getEntity();
        // END IF

        // IF forge-1.16.5
//        ITextComponent localizedDeathMessage = event.getSource().getLocalizedDeathMessage(entity);
//        if (localizedDeathMessage instanceof TranslationTextComponent) {
//            TranslationTextComponent translationTextComponent = (TranslationTextComponent) localizedDeathMessage;
//            deathModel.setKey(translationTextComponent.getKey());
//            String[] args = Arrays.stream(translationTextComponent.getArgs())
//                    .map(arg -> {
//                        if (arg instanceof ITextComponent) {
//                            return ((ITextComponent) arg).getString();
//                        } else {
//                            return String.valueOf(arg);
//                        }
//                    })
//                    .toArray(String[]::new);
//            deathModel.setArgs(args);
//        }
        // ELSE IF forge-1.18.2
//        Component localizedDeathMessage = event.getSource().getLocalizedDeathMessage(entity);
//        if (localizedDeathMessage instanceof TranslatableComponent) {
//            TranslatableComponent translationTextComponent = (TranslatableComponent) localizedDeathMessage;
//            deathModel.setKey(translationTextComponent.getKey());
//            String[] args = Arrays.stream(translationTextComponent.getArgs())
//                    .map(arg -> {
//                        if (arg instanceof Component) {
//                            return ((Component) arg).getString();
//                        } else {
//                            return String.valueOf(arg);
//                        }
//                    })
//                    .toArray(String[]::new);
//            deathModel.setArgs(args);
//        }
        // ELSE
//        Component localizedDeathMessage = event.getSource().getLocalizedDeathMessage(entity);
//        if (localizedDeathMessage instanceof TranslatableContents) {
//            TranslatableContents translationTextComponent = (TranslatableContents) localizedDeathMessage;
//            deathModel.setKey(translationTextComponent.getKey());
//            String[] args = Arrays.stream(translationTextComponent.getArgs())
//                    .map(arg -> {
//                        if (arg instanceof Component) {
//                            return ((Component) arg).getString();
//                        } else {
//                            return String.valueOf(arg);
//                        }
//                    })
//                    .toArray(String[]::new);
//            deathModel.setArgs(args);
//        }
        // END IF
        deathModel.setText(localizedDeathMessage.getString());

        PlayerDeathEvent forgeCommandEvent = new PlayerDeathEvent(player, deathModel);
        GlobalContext.sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    // IF >= forge-1.19
//    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        // ELSE
//    public void onPlayerAdvancement(AdvancementEvent event) {
        // END IF
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        // IF <= forge-1.20.1
//        Advancement advancement = event.getAdvancement();
//        if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceChat()) return;
        // ELSE
//        Advancement advancement = event.getAdvancement().value();
//        if (advancement.display().isEmpty() || !advancement.display().get().shouldAnnounceChat()) return;
        // END IF

        // IF > forge-1.16.5
//        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());
        // ELSE
//        PlayerModel player = getForgePlayer((ServerPlayerEntity) event.getPlayer());
        // END IF

        AchievementModel achievementModel = getForgeAchievement(advancement);
        // IF < forge-1.21
//        String advancementText = player.getNickname() + " has made the advancement " + advancement.getChatComponent().getString();
        // ELSE
//        if (advancement.name().isEmpty()) return;
//        String advancementText = player.getNickname() + " has made the advancement " + advancement.name().get().getString();
        // END IF
        achievementModel.setText(advancementText);

        PlayerAchievementEvent forgeAdvancementEvent = new PlayerAchievementEvent(player, achievementModel);
        GlobalContext.sendEvent(forgeAdvancementEvent);
    }
}