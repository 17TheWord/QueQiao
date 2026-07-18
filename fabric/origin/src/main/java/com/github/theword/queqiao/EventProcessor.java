package com.github.theword.queqiao;

import com.github.theword.queqiao.callback.PlayerAdvancementCallback;
import com.github.theword.queqiao.callback.PlayerCommandCallback;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerAchievementEvent;
import com.github.theword.queqiao.tool.event.PlayerChatEvent;
import com.github.theword.queqiao.tool.event.PlayerCommandEvent;
import com.github.theword.queqiao.tool.event.PlayerDeathEvent;
import com.github.theword.queqiao.tool.event.PlayerJoinEvent;
import com.github.theword.queqiao.tool.event.PlayerQuitEvent;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.utils.FabricTool;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

public class EventProcessor {

    public EventProcessor() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, chatType) -> {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;
            if (!chatType.chatType().is(ChatType.CHAT)) return;

            String rawMessage = message.signedContent();
            String messageText = message.decoratedContent().getString();

            PlayerChatEvent event = new PlayerChatEvent(getFabricPlayer(sender), "", rawMessage, messageText);
            GlobalContext.sendEvent(event);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

            PlayerJoinEvent event = new PlayerJoinEvent(getFabricPlayer(handler.getPlayer()));
            GlobalContext.sendEvent(event);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

            PlayerQuitEvent event = new PlayerQuitEvent(getFabricPlayer(handler.getPlayer()));
            GlobalContext.sendEvent(event);
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
            if (!(entity instanceof ServerPlayer player)) return;

            Component localizedDeathMessage = damageSource.getLocalizedDeathMessage(player);
            TranslateModel translateModel = FabricTool.parseTranslateModel(localizedDeathMessage);

            PlayerDeathEvent event = new PlayerDeathEvent(getFabricPlayer(player), translateModel);
            GlobalContext.sendEvent(event);
        });

        PlayerCommandCallback.EVENT.register((player, command) -> {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

            PlayerCommandEvent event = new PlayerCommandEvent(getFabricPlayer(player), "", command, command);
            GlobalContext.sendEvent(event);
        });

        PlayerAdvancementCallback.EVENT.register((player, advancementHolder) -> {
            if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;

            Advancement advancement = advancementHolder.value();
            if (advancement.display().isEmpty()) return;
            if (!advancement.display().get().shouldAnnounceChat()) return;

            AchievementModel achievementModel = FabricTool.getFabricAchievement(advancementHolder);
            PlayerAchievementEvent event = new PlayerAchievementEvent(getFabricPlayer(player), achievementModel);
            GlobalContext.sendEvent(event);
        });
    }
}
