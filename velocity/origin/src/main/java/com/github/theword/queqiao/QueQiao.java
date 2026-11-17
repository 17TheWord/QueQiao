package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;


@Plugin(id = BaseConstant.MOD_ID, name = BaseConstant.MODULE_NAME, version = BuildConstants.VERSION)
public class QueQiao {

    private final ProxyServer server;

    /**
     * Runtime 在代理初始化时创建；事件与命令通过供应商延迟取值。
     */
    private volatile QueQiaoRuntime runtime;

    private volatile PlatformContext platformContext;

    @Inject
    public QueQiao(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        PlatformContext context = new PlatformContext(server);
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;

        server.getEventManager().register(this, new EventProcessor(this::getPlatformContext));

        CommandMeta meta = server.getCommandManager().metaBuilder(BaseConstant.COMMAND_HEADER)
                .aliases("qq")
                .build();
        server.getCommandManager().register(meta, new CommandExecutor(this::getRuntime));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
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
