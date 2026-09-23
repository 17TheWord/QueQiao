package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public final class QueQiao extends JavaPlugin {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // 与 paper 端一致：监听器与命令在 init 完成后再注册，避免出现
        // 事件已到、上下文未就绪的窗口
        Bukkit.getScheduler().runTask(this, () -> {
            GlobalContext.init(
                    false,
                    instance.getServer().getVersion(),
                    ServerTypeConstant.SPIGOT,
                    new HandleApiImpl(),
                    new HandleCommandReturnMessageImpl()
            );
            Bukkit.getPluginManager().registerEvents(new EventProcessor(), this);

            PluginCommand command = getCommand(BaseConstant.COMMAND_HEADER);
            if (command != null) command.setExecutor(new CommandExecutor());
        });
    }

    @Override
    public void onDisable() {
        GlobalContext.shutdown();
    }
}