package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.PaperTool;
import io.github.theword.queqiao.core.config.ConfigKeys;
import io.github.theword.queqiao.core.event.PlayerAchievementEvent;
import io.github.theword.queqiao.core.event.PlayerChatEvent;
import io.github.theword.queqiao.core.event.PlayerCommandEvent;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import io.papermc.paper.advancement.AdvancementDisplay;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Supplier;

public class EventProcessor implements Listener {

    private final Supplier<PlatformContext> contextSupplier;

    EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerChat(AsyncChatEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (event.isCancelled() || !runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        runtime.sendEvent(new PlayerChatEvent(
                context.getPlayerModel(event.getPlayer()),
                "",
                PaperTool.getComponentJson(event.originalMessage()),
                PaperTool.getComponentText(event.message())
        ));
    }

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;

        Component component = event.deathMessage();
        if (component == null) return;

        TranslateModel translateModel = context.parseTranslateModel(component);
        runtime.sendEvent(new io.github.theword.queqiao.core.event.PlayerDeathEvent(
                context.getPlayerModel(event.getEntity()), translateModel));
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        runtime.sendEvent(new io.github.theword.queqiao.core.event.PlayerJoinEvent(
                context.getPlayerModel(event.getPlayer())));
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

        runtime.sendEvent(new io.github.theword.queqiao.core.event.PlayerQuitEvent(
                context.getPlayerModel(event.getPlayer())));
    }

    @EventHandler
    void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

        String command = runtime.utils.isIgnoredCommand(event.getMessage());
        if (command.isEmpty()) return;

        runtime.sendEvent(new PlayerCommandEvent(context.getPlayerModel(event.getPlayer()), "", command, command));
    }

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

        Advancement advancement = event.getAdvancement();
        if (advancement == null) return;

        AdvancementDisplay display = advancement.getDisplay();
        if (display == null || !display.doesAnnounceToChat() || event.message() == null) return;

        PlayerModel player = context.getPlayerModel(event.getPlayer());
        AchievementModel achievementModel = PaperTool.getPaperAdvancement(context, player.getNickname(), advancement);
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
