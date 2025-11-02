package com.github.theword.queqiao

import com.github.theword.queqiao.event.minecraft.MinecraftPlayer
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerChatEvent
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerJoinEvent
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerQuitEvent
import com.github.theword.queqiao.tool.GlobalContext

class EventProcessor {
    /**
     * 监听玩家聊天
     */
    fun onPlayerChat(playerName: String, message: String) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerChat) return
        val player = MinecraftPlayer(playerName)
        val event = MinecraftPlayerChatEvent("", player, message)
        GlobalContext.sendEvent(event)
    }


    /**
     * 监听玩家加入事件
     */
    fun onPlayerJoin(playerName: String) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerJoin) return
        val player = MinecraftPlayer(playerName)
        val event = MinecraftPlayerJoinEvent(player)
        GlobalContext.sendEvent(event)
    }

    /**
     * 监听玩家离开事件
     */
    fun onPlayerQuit(playerName: String) {
        if (!GlobalContext.getConfig().subscribeEvent.isPlayerQuit) return
        val player = MinecraftPlayer(playerName)
        val event = MinecraftPlayerQuitEvent(player)
        GlobalContext.sendEvent(event)
    }
}