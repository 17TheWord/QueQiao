package com.github.theword.queqiao.event.minecraft

import com.github.theword.queqiao.tool.event.base.BasePlayerJoinEvent

data class MinecraftPlayerJoinEvent(
    private val player: MinecraftPlayer
) : BasePlayerJoinEvent("MinecraftPlayerJoinEvent", player)