package com.github.theword.queqiao

import com.github.theword.queqiao.event.minecraft.MinecraftPlayer
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerChatEvent
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerJoinEvent
import com.github.theword.queqiao.event.minecraft.MinecraftPlayerQuitEvent
import com.github.theword.queqiao.tool.utils.Tool

class EventProcessor {
    /**
     * 监听玩家聊天
     */
    fun onPlayerChat(playerName: String, message: String) {
        if (!Tool.config.subscribeEvent.isPlayerChat) return
        val player = MinecraftPlayer(playerName)
        val event = MinecraftPlayerChatEvent("", player, message)
        Tool.sendWebsocketMessage(event)
    }


    /**
     * 监听玩家加入事件
     */
    fun onPlayerJoin(playerName: String) {
        if (!Tool.config.subscribeEvent.isPlayerJoin) return
        val player = MinecraftPlayer(playerName)
        val event = MinecraftPlayerJoinEvent(player)
        Tool.sendWebsocketMessage(event)
    }

    /**
     * 监听玩家离开事件
     */
    fun onPlayerQuit(playerName: String) {
        if (!Tool.config.subscribeEvent.isPlayerQuit) return
        val player = MinecraftPlayer(playerName)
        val event = MinecraftPlayerQuitEvent(player)
        Tool.sendWebsocketMessage(event)
    }
}
