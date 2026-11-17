package com.github.theword.queqiao.command

import io.github.theword.queqiao.core.command.CommandRouter
import io.github.theword.queqiao.core.constant.CommandConstant
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class CommandExecutor(private val runtimeProvider: () -> QueQiaoRuntime?) : TabExecutor {

    private var cachedRuntime: QueQiaoRuntime? = null

    private var cachedRouter: CommandRouter<CommandSender>? = null

    /**
     * 命令路由要求 Runtime 已启动，因此按当前 Runtime 实例惰性创建并缓存。
     */
    private fun router(runtime: QueQiaoRuntime): CommandRouter<CommandSender> {
        val current = cachedRouter
        if (current != null && cachedRuntime === runtime) {
            return current
        }

        val created = runtime.createCommandRouter<CommandSender>()
        cachedRuntime = runtime
        cachedRouter = created
        return created
    }

    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val runtime = runtimeProvider() ?: return false
        val result = router(runtime).execute(PaperCommandContext(commandSender), args)
        return result == CommandConstant.SUCCESS_SIGNAL
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val runtime = runtimeProvider() ?: return emptyList()
        return router(runtime).tabComplete(PaperCommandContext(commandSender), args)
    }
}
