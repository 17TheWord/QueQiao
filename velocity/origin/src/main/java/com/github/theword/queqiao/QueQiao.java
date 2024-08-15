package com.github.theword.queqiao;

import com.github.theword.queqiao.handle.HandleApiService;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageService;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

@Plugin(id = BaseConstant.MOD_ID, name = BaseConstant.MODULE_NAME, version = BuildConstants.VERSION)
public class QueQiao {

    public static ProxyServer minecraftServer;

    @Inject
    public QueQiao(ProxyServer server) {
        minecraftServer = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        initTool(true, new HandleApiService(), new HandleCommandReturnMessageService());
        websocketManager.startWebsocket(null);
        minecraftServer.getEventManager().register(this, new EventProcessor());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        websocketManager.stopWebsocketByServerClose();
    }

}