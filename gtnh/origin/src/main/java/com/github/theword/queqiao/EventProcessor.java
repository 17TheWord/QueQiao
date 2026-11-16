package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.ForgeTool;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

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

        PlayerModel player = context.getPlayerModel(event.player);
        String rawMessage = IChatComponent.Serializer.func_150696_a(event.component);

        runtime.sendEvent(new PlayerChatEvent(player, "", rawMessage, event.message));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.player);
        runtime.sendEvent(new PlayerJoinEvent(player));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
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
        if (!(event.sender instanceof EntityPlayerMP)) return;

        StringBuilder commandString = new StringBuilder(event.command.getCommandName());
        for (String parameter : event.parameters) {
            commandString.append(" ").append(parameter);
        }

        String command = runtime.utils.isIgnoredCommand(commandString.toString());
        if (command.isEmpty()) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.sender);
        runtime.sendEvent(new PlayerCommandEvent(player, "", command, command));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return;
        if (!(event.entityLiving instanceof EntityPlayerMP)) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.entityLiving);
        IChatComponent deathMessage = event.source.func_151519_b(event.entityLiving);
        TranslateModel translateModel = context.parseTranslateModel(deathMessage);

        runtime.sendEvent(new PlayerDeathEvent(player, translateModel));
    }

    @SubscribeEvent
    public void onPlayerAchievement(AchievementEvent event) {
        PlatformContext context = currentContext();
        if (context == null || event.isCanceled()) return;

        QueQiaoRuntime runtime = context.getRuntime();
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return;

        Achievement achievement = event.achievement;
        if (achievement.statId == null || achievement.statId.isEmpty()
                || achievement.statId.equals("achievement.openInventory")) return;
        if (achievement.func_150951_e() == null) return;

        PlayerModel player = context.getPlayerModel((EntityPlayerMP) event.entityPlayer);
        AchievementModel achievementModel = ForgeTool.getForgeAchievement(context, player.getNickname(), achievement);

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
