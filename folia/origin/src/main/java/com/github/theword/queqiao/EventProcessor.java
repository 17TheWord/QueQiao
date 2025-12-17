package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.event.PlayerAchievementEvent;
import com.github.theword.queqiao.tool.event.PlayerChatEvent;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import com.github.theword.queqiao.tool.event.PlayerCommandEvent;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import com.github.theword.queqiao.tool.utils.Tool;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.server.ServerLoadEvent;

import static com.github.theword.queqiao.utils.FoliaTool.*;
import static com.github.theword.queqiao.utils.FoliaTool.getComponentText;


class EventProcessor implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        GlobalContext.init(
                false,
                QueQiao.instance.getServer().getVersion(),
                ServerTypeConstant.SPIGOT,
                new HandleApiImpl(),
                new HandleCommandReturnMessageImpl()
        );
    }

    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerChat(AsyncChatEvent event) {
        if (event.isCancelled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;
        PlayerChatEvent playerChatEvent = new PlayerChatEvent(getFoliaPlayer(event.getPlayer()), "", getComponentJson(event.message()), getComponentText(event.message()));
        GlobalContext.sendEvent(playerChatEvent);
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
        Component component = event.deathMessage();
        if (component == null) return;

        DeathModel deathModel = new DeathModel();

        if (component instanceof TranslatableComponent translatableComponent) {
            deathModel.setKey(translatableComponent.key());
            String[] args = translatableComponent.arguments().stream().map(arg -> {
                if (arg instanceof TranslationArgument translationArgument) {
                    return translationArgument.toString();
                } else {
                    return String.valueOf(arg);
                }
            }).toArray(String[]::new);
            deathModel.setArgs(args);
        }
        deathModel.setText(getComponentText(component));

        com.github.theword.queqiao.tool.event.PlayerDeathEvent playerDeathEvent = new com.github.theword.queqiao.tool.event.PlayerDeathEvent(getFoliaPlayer(event.getEntity()), deathModel);
        GlobalContext.sendEvent(playerDeathEvent);
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        com.github.theword.queqiao.tool.event.PlayerJoinEvent playerJoinEvent = new com.github.theword.queqiao.tool.event.PlayerJoinEvent(getFoliaPlayer(event.getPlayer()));
        GlobalContext.sendEvent(playerJoinEvent);
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        com.github.theword.queqiao.tool.event.PlayerQuitEvent playerQuitEvent = new com.github.theword.queqiao.tool.event.PlayerQuitEvent(getFoliaPlayer(event.getPlayer()));
        GlobalContext.sendEvent(playerQuitEvent);
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
    @EventHandler
    void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;

        String command = Tool.isIgnoredCommand(event.getMessage());

        if (command.isEmpty()) return;

        PlayerCommandEvent playerCommandEvent = new PlayerCommandEvent(getFoliaPlayer(event.getPlayer()), "", command, command);
        GlobalContext.sendEvent(playerCommandEvent);
    }

    @EventHandler
    void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Advancement advancement = event.getAdvancement();
        AdvancementDisplay advancementDisplay = advancement.getDisplay();
        if (advancementDisplay == null || !advancementDisplay.doesAnnounceToChat()) return;

        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(advancement.getKey().toString());
        achievementModel.setText(getComponentText(event.message()));

        DisplayModel displayModel = new DisplayModel();
        displayModel.setTitle(((TranslatableComponent) advancementDisplay.title()).key());
        displayModel.setDescription(((TranslatableComponent) advancementDisplay.description()).key());
        displayModel.setFrame(advancementDisplay.frame().toString());

        achievementModel.setDisplay(displayModel);

        PlayerAchievementEvent foliaPlayerAdvancementDoneEvent = new PlayerAchievementEvent(getFoliaPlayer(event.getPlayer()), achievementModel);
        GlobalContext.sendEvent(foliaPlayerAdvancementDoneEvent);
    }


}