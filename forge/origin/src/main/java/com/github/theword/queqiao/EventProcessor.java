package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.ForgeTool;
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

// IF forge-1.16.5
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.util.text.ITextComponent;
// ELSE
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.LivingEntity;
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
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
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

        runtime.sendEvent(new PlayerChatEvent(player, "", rawMessage, message));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        // IF > forge-1.16.5
//        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        // ELSE
//        PlayerModel player = context.getPlayerModel((ServerPlayerEntity) event.getEntity());
        // END IF

        runtime.sendEvent(new PlayerJoinEvent(player));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

        // IF > forge-1.16.5
//        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        // ELSE
//        PlayerModel player = context.getPlayerModel((ServerPlayerEntity) event.getPlayer());
        // END IF

        runtime.sendEvent(new PlayerQuitEvent(player));
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

        // IF >= forge-1.19
//        if (!event.getParseResults().getContext().getSource().isPlayer()) return;
        // ELSE IF >= forge-1.18
//        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayer)) return;
        // ELSE
//        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayerEntity)) return;
        // END IF

        String command = runtime.utils.isIgnoredCommand(event.getParseResults().getReader().getString());
        String rawMessage = event.getParseResults().getContext().toString();

        if (command.isEmpty()) return;

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
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;

        // IF > forge-1.16.5
//        if (!(event.getEntity() instanceof ServerPlayer)) return;
//        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        // ELSE
//        if (!(event.getEntity() instanceof ServerPlayerEntity)) return;
//        PlayerModel player = context.getPlayerModel((ServerPlayerEntity) event.getEntity());
        // END IF

        // IF >= forge-1.19
//        LivingEntity entity = event.getEntity();
        // ELSE
//        LivingEntity entity = (LivingEntity) event.getEntity();
        // END IF

        TranslateModel translateModel = context.parseTranslateModel(event.getSource().getLocalizedDeathMessage(entity));

        runtime.sendEvent(new PlayerDeathEvent(player, translateModel));
    }

    @SubscribeEvent
    // IF >= forge-1.19
//    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
    // ELSE
//    public void onPlayerAdvancement(AdvancementEvent event) {
        // END IF
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

        // IF <= forge-1.20.1
//        Advancement advancement = event.getAdvancement();
        // ELSE
//        Advancement advancement = event.getAdvancement().value();
        // END IF

        // IF > forge-1.16.5
//        PlayerModel player = context.getPlayerModel((ServerPlayer) event.getEntity());
        // ELSE
//        PlayerModel player = context.getPlayerModel((ServerPlayerEntity) event.getPlayer());
        // END IF

        AchievementModel achievementModel = ForgeTool.getForgeAchievement(context, player.getNickname(), advancement);
        if (achievementModel == null) return;

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
