package com.github.theword.queqiao;

import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;


@Mod(BaseConstant.MOD_ID)
public class Queqiao {
    public static MinecraftServer minecraftServer;

    public Queqiao() {
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new EventProcessor());
    }

    @SubscribeEvent
    public void ServerStarted(ServerStartedEvent event) {
        minecraftServer = event.getServer();
        GlobalContext.init(true, minecraftServer.getServerVersion(), ServerTypeConstant.NEOFORGE, new HandleApiImpl(), new HandleCommandReturnMessageImpl());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        GlobalContext.shutdown();
    }
}