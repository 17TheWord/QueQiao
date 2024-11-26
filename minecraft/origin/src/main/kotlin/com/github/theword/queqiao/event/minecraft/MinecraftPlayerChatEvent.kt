package com.github.theword.queqiao.event.minecraft

import com.github.theword.queqiao.tool.event.base.BasePlayerChatEvent

data class MinecraftPlayerChatEvent(
    private val messageId: String = "", private val player: MinecraftPlayer, private val message: String
) : BasePlayerChatEvent("MinecraftPlayerChatEvent", messageId, player, message)
