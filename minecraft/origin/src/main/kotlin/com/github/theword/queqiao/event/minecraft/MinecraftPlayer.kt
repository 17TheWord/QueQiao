package com.github.theword.queqiao.event.minecraft

import com.github.theword.queqiao.tool.event.base.BasePlayer

data class MinecraftPlayer(private val nickname: String) : BasePlayer(nickname, null)
