package com.github.theword.queqiao

import com.github.theword.queqiao.tool.GlobalContext
import com.github.theword.queqiao.tool.event.PlayerChatEvent
import com.github.theword.queqiao.tool.event.PlayerJoinEvent
import com.github.theword.queqiao.tool.event.PlayerQuitEvent
import com.github.theword.queqiao.tool.event.model.PlayerModel

class EventProcessor {
    /**
     * 监听玩家聊天
     */
    fun onPlayerChat(playerName: String, message: String) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerChat) return
        val player = PlayerModel(playerName)
        val event = PlayerChatEvent(player, "", message, message)
        GlobalContext.sendEvent(event)
    }


    /**
     * 监听玩家加入事件
     */
    fun onPlayerJoin(playerName: String) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerJoin) return
        val player = PlayerModel(playerName)
        val event = PlayerJoinEvent(player)
        GlobalContext.sendEvent(event)
    }

    /**
     * 监听玩家离开事件
     */
    fun onPlayerQuit(playerName: String) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerQuit) return
        val player = PlayerModel(playerName)
        val event = PlayerQuitEvent(player)
        GlobalContext.sendEvent(event)
    }
}