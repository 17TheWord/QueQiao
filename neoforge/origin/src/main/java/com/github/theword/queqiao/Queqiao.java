package com.github.theword.queqiao;

import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

@Mod(BaseConstant.MOD_ID)
public class Queqiao {
    public static MinecraftServer minecraftServer;

    public Queqiao() {
        initTool(true, new HandleApiImpl(), new HandleCommandReturnMessageImpl());
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new EventProcessor());
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        minecraftServer = event.getServer();
        websocketManager.startWebsocket(null);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStartingEvent event) {
        websocketManager.stopWebsocketByServerClose();
    }
}
