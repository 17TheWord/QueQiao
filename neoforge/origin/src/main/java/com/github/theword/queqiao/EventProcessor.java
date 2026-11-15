package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.NeoForgeTool;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

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
        if (event.isCanceled() || !runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());

        Component message = event.getMessage();
        String rawMessage = NeoForgeTool.parseComponent(message);

        runtime.sendEvent(new PlayerChatEvent(player, "", rawMessage, message.getString()));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        runtime.sendEvent(new PlayerJoinEvent(player));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
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
        if (event.isCanceled() || !runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

        ParseResults<CommandSourceStack> parseResults = event.getParseResults();
        if (!parseResults.getContext().getSource().isPlayer()) return;

        String command = runtime.utils.isIgnoredCommand(parseResults.getReader().getString());
        if (command.isEmpty()) return;

        PlayerModel player;
        try {
            player = context.getPlayerModel(parseResults.getContext().getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return;
        }

        runtime.sendEvent(new PlayerCommandEvent(player, "", parseResults.getContext().toString(), command));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (event.isCanceled() || !runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;

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
        AchievementModel achievementModel = NeoForgeTool.getNeoForgeAchievement(context, player.getNickname(), advancement);
        achievementModel.setKey(event.getAdvancement().id().toString());

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
