package com.github.theword.queqiao;

import com.github.theword.queqiao.callback.PlayerAdvancementCallback;
import com.github.theword.queqiao.callback.PlayerCommandCallback;
import com.github.theword.queqiao.utils.FabricTool;
import io.github.theword.queqiao.core.config.Config;
import io.github.theword.queqiao.core.config.ConfigKeys;
import io.github.theword.queqiao.core.event.PlayerAchievementEvent;
import io.github.theword.queqiao.core.event.PlayerChatEvent;
import io.github.theword.queqiao.core.event.PlayerCommandEvent;
import io.github.theword.queqiao.core.event.PlayerDeathEvent;
import io.github.theword.queqiao.core.event.PlayerJoinEvent;
import io.github.theword.queqiao.core.event.PlayerQuitEvent;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class EventProcessor {

    /**
     * 平台上下文在服务端启动时才创建，事件在模组初始化阶段就注册，因此用供应商延迟取值。
     */
    private final Supplier<PlatformContext> contextSupplier;

    public EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    public void registerEvents() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, chatType) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            Config config = context.getRuntime().getConfig();
            if (!config.get(ConfigKeys.SubscribeEvent.PLAYER_CHAT) || !chatType.chatType().is(ChatType.CHAT)) return;

            String rawMessage = message.signedContent();
            String messageText = message.decoratedContent().getString();

            context.getRuntime().sendEvent(
                    new PlayerChatEvent(context.getPlayerModel(sender), "", rawMessage, messageText));
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            if (!context.getRuntime().getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

            context.getRuntime().sendEvent(
                    new PlayerJoinEvent(context.getPlayerModel(handler.getPlayer())));
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            if (!context.getRuntime().getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

            context.getRuntime().sendEvent(
                    new PlayerQuitEvent(context.getPlayerModel(handler.getPlayer())));
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            if (!context.getRuntime().getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;
            if (!(entity instanceof ServerPlayer player)) return;

            Component localizedDeathMessage = damageSource.getLocalizedDeathMessage(player);
            TranslateModel translateModel = context.parseTranslateModel(localizedDeathMessage);

            context.getRuntime().sendEvent(
                    new PlayerDeathEvent(context.getPlayerModel(player), translateModel));
        });

        PlayerCommandCallback.EVENT.register((player, command) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

            String filteredCommand = runtime.utils.isIgnoredCommand(command);
            if (filteredCommand.isEmpty()) return;

            runtime.sendEvent(
                    new PlayerCommandEvent(context.getPlayerModel(player), "", filteredCommand, filteredCommand));
        });

        PlayerAdvancementCallback.EVENT.register((player, advancementHolder) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

            Advancement advancement = advancementHolder.value();
            if (advancement.display().isEmpty()) return;
            // IF fabric-26.1
//if (!advancement.display().get().shouldAnnounceChat()) return;
            // ELSE IF fabric-26.3
//if (!advancement.display().get().announceToChat()) return;
            // END IF

            AchievementModel achievementModel = FabricTool.getFabricAchievement(context, advancementHolder);
            runtime.sendEvent(
                    new PlayerAchievementEvent(context.getPlayerModel(player), achievementModel));
        });
    }

    /**
     * @return 已就绪的平台上下文；服务端尚未启动时返回 null
     */
    private PlatformContext currentContext() {
        PlatformContext context = contextSupplier.get();
        if (context == null || context.getRuntime() == null) {
            return null;
        }
        return context;
    }
}