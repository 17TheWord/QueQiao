package com.github.theword.queqiao

import com.github.theword.queqiao.tool.GlobalContext
import com.github.theword.queqiao.tool.event.PlayerAchievementEvent
import com.github.theword.queqiao.tool.event.PlayerChatEvent
import com.github.theword.queqiao.tool.event.PlayerCommandEvent
import com.github.theword.queqiao.tool.event.PlayerDeathEvent
import com.github.theword.queqiao.tool.event.PlayerJoinEvent
import com.github.theword.queqiao.tool.event.PlayerQuitEvent
import com.github.theword.queqiao.tool.event.model.PlayerModel
import com.github.theword.queqiao.tool.event.model.TranslateModel
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel
import com.github.theword.queqiao.tool.utils.Tool
import com.github.theword.queqiao.utils.PaperTool.Companion.getComponentJson
import com.github.theword.queqiao.utils.PaperTool.Companion.getComponentText
import com.github.theword.queqiao.utils.PaperTool.Companion.getPaperAdvancement
import com.github.theword.queqiao.utils.PaperTool.Companion.getPaperPlayer
import com.github.theword.queqiao.utils.PaperTool.Companion.parseTranslateModel
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener


class EventProcessor : Listener {


    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: io.papermc.paper.event.player.AsyncChatEvent) {
        if (event.isCancelled || !GlobalContext.getConfig().subscribeEvent.isPlayerChat) return
        val spigotAsyncPlayerChatEvent = PlayerChatEvent(
            getPaperPlayer(event.getPlayer()),
            "",
            getComponentJson(event.originalMessage()),
            getComponentText(event.message())
        )
        GlobalContext.sendEvent(spigotAsyncPlayerChatEvent)
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    fun onPlayerDeath(event: org.bukkit.event.entity.PlayerDeathEvent) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerDeath) return

        val component: Component? = event.deathMessage()

        val translateModel: TranslateModel = parseTranslateModel(component)
        val spigotPlayerDeathEvent = PlayerDeathEvent(getPaperPlayer(event.getEntity()), translateModel)
        GlobalContext.sendEvent(spigotPlayerDeathEvent)
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerJoin) return

        val spigotPlayerJoinEvent = PlayerJoinEvent(getPaperPlayer(event.player))
        GlobalContext.sendEvent(spigotPlayerJoinEvent)
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    fun onPlayerQuit(event: org.bukkit.event.player.PlayerQuitEvent) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerQuit) return

        val spigotPlayerQuitEvent = PlayerQuitEvent(getPaperPlayer(event.player))
        GlobalContext.sendEvent(spigotPlayerQuitEvent)
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
    @EventHandler
    fun onPlayerCommand(event: org.bukkit.event.player.PlayerCommandPreprocessEvent) {
        val command: String = Tool.isIgnoredCommand(event.message)

        if (command.isEmpty()) return

        if (!GlobalContext.getConfig().subscribeEvent.isPlayerCommand) return

        val spigotPlayerCommandPreprocessEvent =
            PlayerCommandEvent(getPaperPlayer(event.getPlayer()), "", command, command)
        GlobalContext.sendEvent(spigotPlayerCommandPreprocessEvent)
    }

    @EventHandler
    fun onPlayerAdvancement(event: org.bukkit.event.player.PlayerAdvancementDoneEvent) {
        val advancement = event.advancement
        if (advancement.display == null || !advancement.display!!.doesAnnounceToChat() || event.message() == null) return
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerAdvancement) return
        val player: PlayerModel = getPaperPlayer(event.getPlayer())
        val achievementModel: AchievementModel = getPaperAdvancement(player.nickname, advancement)

        val spigotPlayerAdvancementDoneEvent = PlayerAchievementEvent(player, achievementModel)
        GlobalContext.sendEvent(spigotPlayerAdvancementDoneEvent)
    }

}