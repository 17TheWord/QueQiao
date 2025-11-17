package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.*;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import com.github.theword.queqiao.tool.utils.Tool;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Arrays;

import static com.github.theword.queqiao.Queqiao.minecraftServer;
import static com.github.theword.queqiao.utils.NeoForgeTool.getNeoForgeAchievement;
import static com.github.theword.queqiao.utils.NeoForgeTool.getNeoForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        PlayerModel player = getNeoForgePlayer(event.getPlayer());

        Component message = event.getMessage();
        String rawMessage = Component.Serializer.toJson(message, minecraftServer.registryAccess());

        PlayerChatEvent NeoForgeServerChatEvent = new PlayerChatEvent(player, "", rawMessage, message.getString());
        GlobalContext.sendEvent(NeoForgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        PlayerModel player = getNeoForgePlayer((ServerPlayer) event.getEntity());

        PlayerJoinEvent forgePlayerLoggedInEvent = new PlayerJoinEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        PlayerModel player = getNeoForgePlayer((ServerPlayer) event.getEntity());

        PlayerQuitEvent forgePlayerLoggedOutEvent = new PlayerQuitEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        ParseResults<CommandSourceStack> parseResults = event.getParseResults();

        if (!parseResults.getContext().getSource().isPlayer()) return;

        String command = Tool.isIgnoredCommand(parseResults.getReader().getString());

        if (command.isEmpty()) return;

        PlayerModel player;

        try {
            player = getNeoForgePlayer(parseResults.getContext().getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return;
        }

        PlayerCommandEvent forgeCommandEvent = new PlayerCommandEvent(player, "", parseResults.getContext().toString(), command);
        GlobalContext.sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        if (!(event.getEntity() instanceof ServerPlayer)) return;
        PlayerModel player = getNeoForgePlayer((ServerPlayer) event.getEntity());

        LivingEntity entity = event.getEntity();

        DeathModel deathModel = new DeathModel();

        Component localizedDeathMessage = event.getSource().getLocalizedDeathMessage(entity);

        if (localizedDeathMessage.getContents() instanceof TranslatableContents translatableContents) {
            deathModel.setKey(translatableContents.getKey());
            String[] args = Arrays.stream(translatableContents.getArgs()).map(obj -> {
                if (obj instanceof Component component) {
                    return component.getString();
                } else {
                    return String.valueOf(obj);
                }
            }).toArray(String[]::new);
            deathModel.setArgs(args);
        }
        deathModel.setText(localizedDeathMessage.getString());

        PlayerDeathEvent forgeCommandEvent = new PlayerDeathEvent(player, deathModel);
        GlobalContext.sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement().value();
        if (advancement.display().isEmpty() || !advancement.display().get().shouldAnnounceChat() || advancement.name().isEmpty())
            return;

        PlayerModel neoForgePlayer = getNeoForgePlayer((ServerPlayer) event.getEntity());

        AchievementModel achievementModel = getNeoForgeAchievement(advancement);
        achievementModel.setKey(event.getAdvancement().id().toString());
        achievementModel.setText(neoForgePlayer.getNickname() + " has made the advancement " + advancement.name().get().getString());

        PlayerAchievementEvent playerAchievementEvent = new PlayerAchievementEvent(neoForgePlayer, achievementModel);
        GlobalContext.sendEvent(playerAchievementEvent);
    }
}