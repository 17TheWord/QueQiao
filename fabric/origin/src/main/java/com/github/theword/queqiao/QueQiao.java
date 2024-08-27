package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

public class QueQiao implements ModInitializer {

    public static MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        initTool(true, new HandleApiImpl(), new HandleCommandReturnMessageImpl());
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            minecraftServer = server;
            websocketManager.startWebsocket(null);
        });

        new CommandExecutor();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> websocketManager.stopWebsocketByServerClose());
    }
}