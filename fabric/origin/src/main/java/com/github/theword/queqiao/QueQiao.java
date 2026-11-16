package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;


public class QueQiao implements ModInitializer {

    public static MinecraftServer minecraftServer;

    /**
     * Runtime 在服务端启动后才存在；事件与命令通过供应商延迟取值。
     */
    private volatile QueQiaoRuntime runtime;

    private volatile PlatformContext platformContext;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            minecraftServer = server;
            PlatformContext context = new PlatformContext(server);
            QueQiaoRuntime created = QueQiaoRuntime.create(context);
            context.bindRuntime(created);
            created.start();

            platformContext = context;
            runtime = created;
        });

        new EventProcessor(this::getPlatformContext).registerEvents();
        new CommandExecutor(this::getRuntime);

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            QueQiaoRuntime current = runtime;
            if (current != null) {
                current.shutdown();
            }
            runtime = null;
            platformContext = null;
        });
    }

    public QueQiaoRuntime getRuntime() {
        return runtime;
    }

    public PlatformContext getPlatformContext() {
        return platformContext;
    }
}
