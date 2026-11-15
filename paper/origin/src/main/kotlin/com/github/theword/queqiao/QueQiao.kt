package com.github.theword.queqiao

import com.github.theword.queqiao.command.CommandExecutor
import io.github.theword.queqiao.core.constant.BaseConstant
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class QueQiao : JavaPlugin() {

    companion object {
        lateinit var instance: JavaPlugin
            private set
    }

    /**
     * Runtime 在首个主线程任务中创建；事件与命令通过供应商延迟取值。
     */
    private var runtime: QueQiaoRuntime? = null

    private var platformContext: PlatformContext? = null

    override fun onEnable() {
        instance = this

        Bukkit.getScheduler().runTask(this, Runnable {
            val context = PlatformContext(server)
            val created = QueQiaoRuntime.create(context)
            context.bindRuntime(created)
            created.start()

            platformContext = context
            runtime = created
        })

        Bukkit.getPluginManager().registerEvents(EventProcessor { platformContext }, this)
        getCommand(BaseConstant.COMMAND_HEADER)?.setExecutor(CommandExecutor { runtime })
    }

    override fun onDisable() {
        runtime?.shutdown()
    }
}
