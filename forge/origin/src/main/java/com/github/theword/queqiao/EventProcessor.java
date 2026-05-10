package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.*;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.utils.ForgeTool;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import static com.github.theword.queqiao.tool.utils.Tool.isIgnoredCommand;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAchievement;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        PlayerModel player = getForgePlayer(event.getPlayer());
        String message = event.getMessage().getString();
        String rawMessage = ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, event.getMessage()).getOrThrow().toString();

        PlayerChatEvent forgeServerChatEvent = new PlayerChatEvent(player, "", rawMessage, message);
        GlobalContext.sendEvent(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());

        PlayerJoinEvent forgePlayerLoggedInEvent = new PlayerJoinEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());

        PlayerQuitEvent forgePlayerLoggedOutEvent = new PlayerQuitEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        if (!event.getParseResults().getContext().getSource().isPlayer()) return;

        String command = isIgnoredCommand(event.getParseResults().getReader().getString());
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
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        if (!(event.getEntity() instanceof ServerPlayer)) return;
        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());
        LivingEntity entity = event.getEntity();

        TranslateModel translateModel = ForgeTool.parseTranslateModel(event.getSource().getLocalizedDeathMessage(entity));

        PlayerDeathEvent forgeCommandEvent = new PlayerDeathEvent(player, translateModel);
        GlobalContext.sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement().value();
        if (advancement.display().isEmpty() || !advancement.display().get().shouldAnnounceChat() || advancement.name().isEmpty()) return;
        PlayerModel player = getForgePlayer((ServerPlayer) event.getEntity());

        AchievementModel achievementModel = getForgeAchievement(player.getNickname(), advancement);

        PlayerAchievementEvent forgeAdvancementEvent = new PlayerAchievementEvent(player, achievementModel);
        GlobalContext.sendEvent(forgeAdvancementEvent);
    }
}