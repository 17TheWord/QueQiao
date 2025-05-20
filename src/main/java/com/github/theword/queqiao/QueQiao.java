package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandTreeQueQiao;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

@Mod(modid = BaseConstant.MOD_ID, version = QueQiao.VERSION, name = BaseConstant.MODULE_NAME, acceptableRemoteVersions = "*", serverSideOnly = false, clientSideOnly = false)
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

//    @SideOnly(Side.SERVER)
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();
        initTool(true, "1.12.2", ServerTypeConstant.FORGE, new HandleApiImpl(), new HandleCommandReturnMessageImpl());
        websocketManager.startWebsocketOnServerStart();

        CommandTreeQueQiao command = new CommandTreeQueQiao();
        event.registerServerCommand(command);
    }

//    @SideOnly(Side.SERVER)
    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        websocketManager.stopWebsocketByServerClose();
    }

}
