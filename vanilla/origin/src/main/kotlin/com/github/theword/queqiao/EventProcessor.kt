package com.github.theword.queqiao

import io.github.theword.queqiao.core.config.ConfigKeys
import io.github.theword.queqiao.core.event.PlayerChatEvent
import io.github.theword.queqiao.core.event.PlayerJoinEvent
import io.github.theword.queqiao.core.event.PlayerQuitEvent
import io.github.theword.queqiao.core.event.model.PlayerModel
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime

class EventProcessor(private val runtime: QueQiaoRuntime) {

    /**
     * 监听玩家聊天
     */
    fun onPlayerChat(playerName: String, message: String) {
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_CHAT)) return
        val player = PlayerModel(playerName)
        // 日志里没有可靠的原始组件 JSON，raw_message 留空
        runtime.sendEvent(PlayerChatEvent(player, "", "", message))
    }

    /**
     * 监听玩家加入事件
     */
    fun onPlayerJoin(playerName: String) {
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_JOIN)) return
        runtime.sendEvent(PlayerJoinEvent(PlayerModel(playerName)))
    }

    /**
     * 监听玩家离开事件
     */
    fun onPlayerQuit(playerName: String) {
        if (!runtime.getConfig().get(ConfigKeys.SubscribeEvent.PLAYER_QUIT)) return
        runtime.sendEvent(PlayerQuitEvent(PlayerModel(playerName)))
    }
}
