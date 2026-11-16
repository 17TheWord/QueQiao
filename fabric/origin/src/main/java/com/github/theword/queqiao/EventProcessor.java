package com.github.theword.queqiao;

import com.github.theword.queqiao.callback.PlayerAdvancementCallback;
import com.github.theword.queqiao.callback.PlayerChatCallback;
import com.github.theword.queqiao.callback.PlayerCommandCallback;
import com.github.theword.queqiao.callback.PlayerDeathCallback;
import com.github.theword.queqiao.callback.PlayerJoinCallback;
import com.github.theword.queqiao.callback.PlayerQuitCallback;
import com.github.theword.queqiao.utils.FabricTool;
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
import net.minecraft.text.Text;

import java.util.function.Supplier;

/**
 * 事件统一处理层：把平台回调转换为 core 事件。
 */
public class EventProcessor {

    /**
     * 平台上下文在服务端启动时才创建，事件在模组初始化阶段就注册，因此用供应商延迟取值。
     */
    private final Supplier<PlatformContext> contextSupplier;

    public EventProcessor(Supplier<PlatformContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    public void registerEvents() {
        PlayerChatCallback.EVENT.register((player, message) -> {
            if (message.startsWith("/")) return;

            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return;

            runtime.sendEvent(new PlayerChatEvent(context.getPlayerModel(player), "", message, message));
        });

        PlayerJoinCallback.EVENT.register(player -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

            runtime.sendEvent(new PlayerJoinEvent(context.getPlayerModel(player)));
        });

        PlayerQuitCallback.EVENT.register(player -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return;

            runtime.sendEvent(new PlayerQuitEvent(context.getPlayerModel(player)));
        });

        PlayerDeathCallback.EVENT.register(player -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;

            Text deathMessage = player.getDamageTracker().getDeathMessage();
            TranslateModel translateModel = context.parseTranslateModel(deathMessage);

            runtime.sendEvent(new PlayerDeathEvent(context.getPlayerModel(player), translateModel));
        });

        PlayerCommandCallback.EVENT.register((player, command) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return;

            String filteredCommand = runtime.utils.isIgnoredCommand(command);
            if (filteredCommand.isEmpty()) return;

            runtime.sendEvent(new PlayerCommandEvent(context.getPlayerModel(player), "", filteredCommand, filteredCommand));
        });

        PlayerAdvancementCallback.EVENT.register((player, advancement) -> {
            PlatformContext context = currentContext();
            if (context == null) return;

            QueQiaoRuntime runtime = context.getRuntime();
            if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

            AchievementModel achievementModel = FabricTool.getFabricAchievement(context, player, advancement);
            if (achievementModel == null) return;

            runtime.sendEvent(new PlayerAchievementEvent(context.getPlayerModel(player), achievementModel));
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
