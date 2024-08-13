package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(id = BaseConstant.MOD_ID, name = BaseConstant.MODULE_NAME, version = BuildConstants.VERSION)
public class QueQiao {

    private final ProxyServer minecraftServer;

    @Inject
    public QueQiao(ProxyServer server, Logger logger) {
        this.minecraftServer = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }

}
