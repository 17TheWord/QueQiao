package com.github.theword.queqiao.command

import io.github.theword.queqiao.core.command.CommandExecutionContext
import org.bukkit.command.CommandSender

/**
 * 一次命令调用的执行上下文（Bukkit 版）。
 */
class PaperCommandContext(nativeSource: CommandSender) :
    CommandExecutionContext<CommandSender>(nativeSource) {

    override fun reply(message: String) {
        nativeSource.sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return nativeSource.hasPermission(permission)
    }
}
