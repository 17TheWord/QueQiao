package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.ForgeTool;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import io.github.theword.queqiao.core.config.ConfigKeys;
import io.github.theword.queqiao.core.event.PlayerAchievementEvent;
import io.github.theword.queqiao.core.event.PlayerChatEvent;
import io.github.theword.queqiao.core.event.PlayerCommandEvent;
import io.github.theword.queqiao.core.event.PlayerDeathEvent;
import io.github.theword.queqiao.core.event.PlayerJoinEvent;
import io.github.theword.queqiao.core.event.PlayerQuitEvent;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
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

import java.util.function.Supplier;

public class EventProcessor {

    /**
     * 平台上下文在服务端启动时才创建，事件在模组构造阶段就注册，因此用供应商延迟取值。
     */
    private final Supplier<PlatformContext> contextSupplier;

    public EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
        String message = event.getMessage().getString();
        String rawMessage = ComponentSerialization.CODEC
                .encodeStart(JsonOps.INSTANCE, event.getMessage()).getOrThrow().toString();

        runtime.sendEvent(new PlayerChatEvent(player, "", rawMessage, message));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        runtime.sendEvent(new PlayerJoinEvent(player));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        runtime.sendEvent(new PlayerQuitEvent(player));
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

        if (!event.getParseResults().getContext().getSource().isPlayer()) return;

        String command = runtime.utils.isIgnoredCommand(event.getParseResults().getReader().getString());
        if (command.isEmpty()) return;

        String rawMessage = event.getParseResults().getContext().toString();

        PlayerModel player;
        try {
            player = context.getPlayerModel(event.getParseResults().getContext().getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return;
        }

        runtime.sendEvent(new PlayerCommandEvent(player, "", rawMessage, command));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;

        if (!(event.getEntity() instanceof ServerPlayer)) return;

        LivingEntity entity = event.getEntity();
        PlayerModel player = context.getPlayerModel((ServerPlayer) entity);
        TranslateModel translateModel = context.parseTranslateModel(event.getSource().getLocalizedDeathMessage(entity));

        runtime.sendEvent(new PlayerDeathEvent(player, translateModel));
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

        Advancement advancement = event.getAdvancement().value();
        if (advancement.display().isEmpty()
                || !advancement.display().get().shouldAnnounceChat()
                || advancement.name().isEmpty()) return;

        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        AchievementModel achievementModel = ForgeTool.getForgeAchievement(context, player.getNickname(), advancement);

        runtime.sendEvent(new PlayerAchievementEvent(player, achievementModel));
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
