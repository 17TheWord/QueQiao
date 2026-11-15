package com.github.theword.queqiao

import com.github.theword.queqiao.utils.PaperTool.Companion.getComponentJson
import com.github.theword.queqiao.utils.PaperTool.Companion.getComponentText
import com.github.theword.queqiao.utils.PaperTool.Companion.getPaperAdvancement
import io.github.theword.queqiao.core.config.ConfigKeys
import io.github.theword.queqiao.core.event.PlayerAchievementEvent
import io.github.theword.queqiao.core.event.PlayerChatEvent
import io.github.theword.queqiao.core.event.PlayerCommandEvent
import io.github.theword.queqiao.core.event.PlayerDeathEvent
import io.github.theword.queqiao.core.event.PlayerJoinEvent
import io.github.theword.queqiao.core.event.PlayerQuitEvent
import io.github.theword.queqiao.core.event.model.PlayerModel
import io.github.theword.queqiao.core.event.model.TranslateModel
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener


class EventProcessor(private val contextProvider: () -> PlatformContext?) : Listener {

    /**
     * 平台上下文在插件启用后由主线程任务创建，事件在此之前可能已被触发，因此每次取值。
     */
    private fun context(): PlatformContext? = contextProvider()?.takeIf { it.runtime != null }

    /**
     * 监听玩家聊天
     *
     * @param event 玩家聊天事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: io.papermc.paper.event.player.AsyncChatEvent) {
        val context = context() ?: return
        val runtime = context.runtime ?: return

        if (event.isCancelled || !runtime.config.get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return

        runtime.sendEvent(
            PlayerChatEvent(
                context.getPlayerModel(event.player),
                "",
                getComponentJson(event.originalMessage()),
                getComponentText(event.message())
            )
        )
    }

    /**
     * 监听玩家死亡事件
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    fun onPlayerDeath(event: org.bukkit.event.entity.PlayerDeathEvent) {
        val context = context() ?: return
        val runtime = context.runtime ?: return

        if (!runtime.config.get(ConfigKeys.SubscribeEvent.PLAYER_DEATH)) return

        val component = event.deathMessage() ?: return
        val translateModel: TranslateModel = context.parseTranslateModel(component)

        runtime.sendEvent(PlayerDeathEvent(context.getPlayerModel(event.entity), translateModel))
    }

    /**
     * 监听玩家加入事件
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        val context = context() ?: return
        val runtime = context.runtime ?: return

        if (!runtime.config.get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return

        runtime.sendEvent(PlayerJoinEvent(context.getPlayerModel(event.player)))
    }

    /**
     * 监听玩家离开事件
     *
     * @param event 玩家离开事件
     */
    @EventHandler
    fun onPlayerQuit(event: org.bukkit.event.player.PlayerQuitEvent) {
        val context = context() ?: return
        val runtime = context.runtime ?: return

        if (!runtime.config.get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return

        runtime.sendEvent(PlayerQuitEvent(context.getPlayerModel(event.player)))
    }

    /**
     * 监听玩家命令
     *
     * @param event 玩家命令事件
     */
    @EventHandler
    fun onPlayerCommand(event: org.bukkit.event.player.PlayerCommandPreprocessEvent) {
        val context = context() ?: return
        val runtime = context.runtime ?: return

        if (!runtime.config.get(ConfigKeys.SubscribeEvent.PLAYER_COMMAND)) return

        val command: String = runtime.utils.isIgnoredCommand(event.message)
        if (command.isEmpty()) return

        runtime.sendEvent(PlayerCommandEvent(context.getPlayerModel(event.player), "", command, command))
    }

    @EventHandler
    fun onPlayerAdvancement(event: org.bukkit.event.player.PlayerAdvancementDoneEvent) {
        val context = context() ?: return
        val runtime = context.runtime ?: return

        val advancement = event.advancement
        if (advancement.display == null || !advancement.display!!.doesAnnounceToChat() || event.message() == null) return
        if (!runtime.config.get(ConfigKeys.SubscribeEvent.PLAYER_ADVANCEMENT)) return

        val player: PlayerModel = context.getPlayerModel(event.player)
        val achievementModel: AchievementModel = getPaperAdvancement(context, player.nickname, advancement)

        runtime.sendEvent(PlayerAchievementEvent(player, achievementModel))
    }
}
