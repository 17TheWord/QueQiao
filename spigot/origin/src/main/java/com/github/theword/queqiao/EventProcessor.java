package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.SpigotTool;
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
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.function.Supplier;


class EventProcessor implements Listener {

    /**
     * 平台上下文在插件启用后由主线程任务创建，事件在此之前可能已被触发，因此每次取值。
     */
    private final Supplier<PlatformContext> contextSupplier;

    EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerChat(AsyncPlayerChatEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (event.isCancelled() || !runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

        String message = event.getMessage();
        runtime.sendEvent(new PlayerChatEvent(context.getPlayerModel(event.getPlayer()), "", message, message));
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;

        // Spigot 没有结构化消息组件，死亡信息只能按纯文本上报
        TranslateModel translateModel = new TranslateModel(null, null, event.getDeathMessage());

        runtime.sendEvent(new PlayerDeathEvent(context.getPlayerModel(event.getEntity()), translateModel));
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        runtime.sendEvent(new PlayerJoinEvent(context.getPlayerModel(event.getPlayer())));
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        PlatformContext context = currentContext();
        if (context == null) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

        runtime.sendEvent(new PlayerQuitEvent(context.getPlayerModel(event.getPlayer())));
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
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

        AdvancementDisplay display = event.getAdvancement().getDisplay();
        if (display == null) return;

        // 只上报"会公告到聊天栏"的成就；隐藏成就（如配方解锁）不上报
        if (display.isHidden() || !display.shouldAnnounceChat()) return;

        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;
        Advancement advancement = event.getAdvancement();

        AchievementModel achievementModel = SpigotTool.getSpigotAdvancementDisplay(display);

        runtime.sendEvent(new PlayerAchievementEvent(context.getPlayerModel(event.getPlayer()), achievementModel));
    }

    /**
     * @return 已就绪的平台上下文；插件尚未完成初始化时返回 null
     */
    private PlatformContext currentContext() {
        PlatformContext context = contextSupplier.get();
        if (context == null || context.getRuntime() == null) {
            return null;
        }
        return context;
    }
}
