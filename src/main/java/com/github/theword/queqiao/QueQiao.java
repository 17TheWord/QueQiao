package com.github.theword.queqiao;

import com.github.theword.queqiao.tool.GlobalContext;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
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
        // MinecraftForge.EVENT_BUS.register(new EventProcessor());
        // FMLCommonHandler.instance().bus().register(new EventProcessor());
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void FMLServerStarted(FMLServerStartedEvent event) {
        GlobalContext.init(
            true,
            minecraftServer.getMinecraftVersion(),
            ServerTypeConstant.FORGE,
            new HandleApiImpl(),
            new HandleCommandReturnMessageImpl());
        // var eventProcessor = new EventProcessor();
        MinecraftForge.EVENT_BUS.register(new EventProcessor());
        FMLCommonHandler.instance().bus().register(new EventProcessor());
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStopping(FMLServerStoppingEvent event) {
        GlobalContext.shutdown();
    }
}
