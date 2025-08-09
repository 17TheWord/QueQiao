package com.github.theword.queqiao;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(
    modid = BaseConstant.MOD_ID,
    name = BaseConstant.MODULE_NAME,
    version = Tags.VERSION,
    acceptableRemoteVersions = "*",
    acceptedMinecraftVersions = "[1.7.10]",
    useMetadata = true
)
public class QueQiao {

    public static MinecraftServer minecraftServer;

    public QueQiao() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventProcessor());
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();
        initTool(
            true,
            minecraftServer.getMinecraftVersion(),
            ServerTypeConstant.FORGE,
            new HandleApiImpl(),
            new HandleCommandReturnMessageImpl());
        websocketManager.startWebsocketOnServerStart();
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStopping(FMLServerStoppingEvent event) {
        websocketManager.stopWebsocketByServerClose();
    }
}
