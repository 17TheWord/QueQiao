package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;


public class QueQiao implements ModInitializer {

    public static MinecraftServer minecraftServer;


    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            minecraftServer = server;
            GlobalContext.init(
                    true,
                    minecraftServer.getVersion(),
                    ServerTypeConstant.FABRIC,
                    new HandleApiImpl(),
                    new HandleCommandReturnMessageImpl()
            );
        });

        new CommandExecutor();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> GlobalContext.shutdown());
    }
}