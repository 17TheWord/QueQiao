package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.google.inject.Inject;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;


@Plugin(id = BaseConstant.MOD_ID, name = BaseConstant.MODULE_NAME, version = BuildConstants.VERSION)
public class QueQiao {

    public static ProxyServer minecraftServer;

    private volatile QueQiaoRuntime runtime;

    private volatile PlatformContext platformContext;

    @Inject
    public QueQiao(ProxyServer server) {
        minecraftServer = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        PlatformContext context = new PlatformContext(minecraftServer);
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;

        minecraftServer.getEventManager().register(this, new EventProcessor(this::getPlatformContext));
        CommandMeta meta = minecraftServer.getCommandManager().metaBuilder(BaseConstant.COMMAND_HEADER)
                .aliases("qq")
                .build();
        minecraftServer.getCommandManager().register(meta, new CommandExecutor(this::getRuntime));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        QueQiaoRuntime current = runtime;
        if (current != null) {
            current.shutdown();
        }
        runtime = null;
        platformContext = null;
    }

    public QueQiaoRuntime getRuntime() {
        return runtime;
    }

    public PlatformContext getPlatformContext() {
        return platformContext;
    }
}
