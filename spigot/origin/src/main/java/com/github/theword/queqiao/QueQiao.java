package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueQiao extends JavaPlugin {

    public static JavaPlugin instance;

    /**
     * Runtime 在首个主线程任务中创建；事件与命令通过供应商延迟取值。
     */
    private volatile QueQiaoRuntime runtime;

    private volatile PlatformContext platformContext;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getScheduler().runTask(this, () -> {
            PlatformContext context = new PlatformContext(instance.getServer());
            QueQiaoRuntime created = QueQiaoRuntime.create(context);
            context.bindRuntime(created);
            created.start();

            platformContext = context;
            runtime = created;
        });

        Bukkit.getPluginManager().registerEvents(new EventProcessor(this::getPlatformContext), this);

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

    public QueQiaoRuntime getRuntime() {
        return runtime;
    }

    public PlatformContext getPlatformContext() {
        return platformContext;
    }
}
