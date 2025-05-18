package com.github.theword.queqiao;

import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

@Mod(modid = BaseConstant.MOD_ID, version = QueQiao.VERSION, name = BaseConstant.MODULE_NAME, acceptableRemoteVersions = "*", serverSideOnly = true, clientSideOnly = false)
public class QueQiao {
    public static final String VERSION = "@VERSION@";
    public static MinecraftServer minecraftServer;

    public QueQiao() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventProcessor());
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @SideOnly(Side.SERVER)
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();
        initTool(true, "1.12.2", ServerTypeConstant.FORGE, new HandleApiImpl(), new HandleCommandReturnMessageImpl());
        websocketManager.startWebsocketOnServerStart();
    }

    @SideOnly(Side.SERVER)
    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        websocketManager.stopWebsocketByServerClose();
    }

}
