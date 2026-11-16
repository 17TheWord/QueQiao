package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.ForgeTool;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
        String rawMessage = ITextComponent.Serializer.componentToJson(event.getComponent());

        runtime.sendEvent(new PlayerChatEvent(player, "", rawMessage, event.getMessage()));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.player);
        runtime.sendEvent(new PlayerJoinEvent(player));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.player);
        runtime.sendEvent(new PlayerQuitEvent(player));
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;
        if (!(event.getSender() instanceof EntityPlayerMP)) return;

        StringBuilder commandString = new StringBuilder(event.getCommand().getName());
        for (String parameter : event.getParameters()) {
            commandString.append(" ").append(parameter);
        }

        String command = runtime.utils.isIgnoredCommand(commandString.toString());
        if (command.isEmpty()) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.getSender());
        runtime.sendEvent(new PlayerCommandEvent(player, "", command, command));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.getEntity());
        ITextComponent deathMessage = event.getEntityLiving().getCombatTracker().getDeathMessage();
        TranslateModel translateModel = context.parseTranslateModel(deathMessage);

        runtime.sendEvent(new PlayerDeathEvent(player, translateModel));
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

        Advancement advancement = event.getAdvancement();
        if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceToChat()) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.getEntityPlayer());
        AchievementModel achievementModel = ForgeTool.getForgeAchievement(context, player.getNickname(), advancement);

        runtime.sendEvent(new PlayerAchievementEvent(player, achievementModel));
    }

    private PlatformContext currentContext() {
        PlatformContext context = contextSupplier.get();
        if (context == null || context.getRuntime() == null) {
            return null;
        }
        return context;
    }
}
