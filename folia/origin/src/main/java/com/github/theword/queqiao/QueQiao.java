package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueQiao extends JavaPlugin {

    public static JavaPlugin instance;

    private volatile QueQiaoRuntime runtime;

    private volatile PlatformContext platformContext;

    @Override
    public void onEnable() {
        // Folia 中插件加载过早，工具包初始化位于 EventProcessor 的 ServerLoadEvent 事件
        instance = this;

        Bukkit.getPluginManager().registerEvents(new EventProcessor(this), this);

        PluginCommand command = getCommand(BaseConstant.COMMAND_HEADER);
        if (command != null) command.setExecutor(new CommandExecutor(this::getRuntime));
    }

    @Override
    public void onDisable() {
        QueQiaoRuntime current = runtime;
        if (current != null) {
            current.shutdown();
        }
    }

    /**
     * 在服务器加载完成后初始化 core 运行时（仅执行一次）。
     */
    public synchronized void initRuntime() {
        if (platformContext != null) return;

        PlatformContext context = new PlatformContext(instance.getServer());
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;
    }

    public QueQiaoRuntime getRuntime() {
        return runtime;
    }

    public PlatformContext getPlatformContext() {
        return platformContext;
    }
}
