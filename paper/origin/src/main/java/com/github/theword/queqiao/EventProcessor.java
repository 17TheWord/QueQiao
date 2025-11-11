package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerAchievementEvent;
import com.github.theword.queqiao.tool.event.PlayerCommandEvent;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
import static com.github.theword.queqiao.utils.PaperTool.*;

public class EventProcessor implements Listener {

    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerChat(AsyncChatEvent event) {
        if (event.isCancelled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;
        com.github.theword.queqiao.tool.event.PlayerChatEvent spigotAsyncPlayerChatEvent = new com.github.theword.queqiao.tool.event.PlayerChatEvent(getPaperPlayer(event.getPlayer()), "", getComponentJson(event.originalMessage()), getComponentText(event.message()));
        GlobalContext.sendEvent(spigotAsyncPlayerChatEvent);
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        DeathModel deathModel = new DeathModel();
        Component component = event.deathMessage();
        if (component instanceof TranslatableComponent translatableComponent) {
            deathModel.setKey(translatableComponent.key());
            String[] args = translatableComponent.args().stream()
                    .map(arg -> {
                        if (arg instanceof TextComponent) {
                            return ((TextComponent) arg).content();
                        } else {
                            return String.valueOf(arg);
                        }
                    })
                    .toArray(String[]::new);
            deathModel.setArgs(args);
        }

        deathModel.setText(getComponentText(component));

        com.github.theword.queqiao.tool.event.PlayerDeathEvent spigotPlayerDeathEvent = new com.github.theword.queqiao.tool.event.PlayerDeathEvent(getPaperPlayer(event.getEntity()), deathModel);
        GlobalContext.sendEvent(spigotPlayerDeathEvent);
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        com.github.theword.queqiao.tool.event.PlayerJoinEvent spigotPlayerJoinEvent = new com.github.theword.queqiao.tool.event.PlayerJoinEvent(getPaperPlayer(event.getPlayer()));
        GlobalContext.sendEvent(spigotPlayerJoinEvent);
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        com.github.theword.queqiao.tool.event.PlayerQuitEvent spigotPlayerQuitEvent = new com.github.theword.queqiao.tool.event.PlayerQuitEvent(getPaperPlayer(event.getPlayer()));
        GlobalContext.sendEvent(spigotPlayerQuitEvent);
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
    @EventHandler
    void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        String command = isRegisterOrLoginCommand(event.getMessage());

        if (command.isEmpty()) return;

        PlayerCommandEvent spigotPlayerCommandPreprocessEvent = new PlayerCommandEvent(getPaperPlayer(event.getPlayer()), "", command, command);
        GlobalContext.sendEvent(spigotPlayerCommandPreprocessEvent);
    }

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement();
        if (advancement.getDisplay() == null || !advancement.getDisplay().doesAnnounceToChat() || event.message() == null)
            return;

        AchievementModel achievementModel = getPaperAdvancement(advancement);
        achievementModel.setText(getComponentText(event.message()));

        PlayerAchievementEvent spigotPlayerAdvancementDoneEvent = new PlayerAchievementEvent(getPaperPlayer(event.getPlayer()), achievementModel);
        GlobalContext.sendEvent(spigotPlayerAdvancementDoneEvent);
    }
}