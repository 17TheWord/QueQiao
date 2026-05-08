package com.github.theword.queqiao

import com.github.theword.queqiao.command.CommandExecutor
import com.github.theword.queqiao.handle.HandleApiImpl
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl
import com.github.theword.queqiao.tool.GlobalContext
import com.github.theword.queqiao.tool.constant.BaseConstant
import com.github.theword.queqiao.tool.constant.ServerTypeConstant
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class QueQiao : JavaPlugin() {

    companion object {
        lateinit var instance: JavaPlugin
            private set
    }

    override fun onEnable() {
        instance = this
        Bukkit.getScheduler().runTask(this, Runnable {
            GlobalContext.init(
                false,
                server.minecraftVersion,
                ServerTypeConstant.PAPER,
                HandleApiImpl(),
                HandleCommandReturnMessageImpl()
            )
        })
        Bukkit.getPluginManager().registerEvents(EventProcessor(), this)
        val command = getCommand(BaseConstant.COMMAND_HEADER)
        command?.setExecutor(CommandExecutor())
    }

    override fun onDisable() {
        GlobalContext.shutdown()
    }
}