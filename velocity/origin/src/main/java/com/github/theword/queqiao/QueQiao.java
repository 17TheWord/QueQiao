package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;


@Plugin(id = BaseConstant.MOD_ID, name = BaseConstant.MODULE_NAME, version = BuildConstants.VERSION)
public class QueQiao {

    public static ProxyServer minecraftServer;

    @Inject
    public QueQiao(ProxyServer server) {
        minecraftServer = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        GlobalContext.init(
                false,
                minecraftServer.getVersion().getVersion(),
                ServerTypeConstant.VELOCITY,
                new HandleApiImpl(),
                new HandleCommandReturnMessageImpl()
        );
        minecraftServer.getEventManager().register(this, new EventProcessor());
        // 注册命令（新版API）
        CommandExecutor commandExecutor = new CommandExecutor();
        CommandMeta meta = minecraftServer.getCommandManager().metaBuilder(BaseConstant.COMMAND_HEADER)
                .aliases("qq")
                .build();
        minecraftServer.getCommandManager().register(meta, commandExecutor);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        GlobalContext.shutdown();
    }

}