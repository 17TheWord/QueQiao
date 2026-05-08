package com.github.theword.queqiao.command

import com.github.theword.queqiao.tool.command.CommandExecutorHelper
import com.github.theword.queqiao.tool.constant.CommandConstant
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class CommandExecutor : TabExecutor {

    private val commandExecutorHelper = CommandExecutorHelper()

    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val result = commandExecutorHelper.execute(commandSender, args)
        return result == CommandConstant.SUCCESS_SIGNAL
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return commandExecutorHelper.tabComplete(commandSender, args)
    }
}