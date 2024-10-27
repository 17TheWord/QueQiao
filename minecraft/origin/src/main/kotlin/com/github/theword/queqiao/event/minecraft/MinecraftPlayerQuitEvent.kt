package com.github.theword.queqiao.event.minecraft

import com.github.theword.queqiao.tool.event.base.BasePlayerQuitEvent

data class MinecraftPlayerQuitEvent(
    private val player: MinecraftPlayer
) : BasePlayerQuitEvent("MinecraftPlayerQuitEvent", player)