package com.github.theword.queqiao;

import com.github.theword.queqiao.handle.HandleApiService;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageService;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;


@Mod(BaseConstant.MOD_ID)
public class QueQiao {
    public static MinecraftServer minecraftServer;

    public QueQiao() {
        initTool(true, new HandleApiService(), new HandleCommandReturnMessageService());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventProcessor());
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    // IF > forge-1.16.5
//    public void onServerStarted(net.minecraftforge.event.server.ServerStartedEvent event) {
        // ELSE
//    public void onServerStarted(net.minecraftforge.fml.event.server.FMLServerStartedEvent event) {
        // END IF
        minecraftServer = event.getServer();
        websocketManager.startWebsocket(null);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    // IF > forge-1.16.5
//    public void onServerStopping(net.minecraftforge.event.server.ServerStoppingEvent event) {
        // ELSE
//    public void onServerStopping(net.minecraftforge.fml.event.server.FMLServerStoppingEvent event) {
        // END IF
        websocketManager.stopWebsocketByServerClose();
    }
}