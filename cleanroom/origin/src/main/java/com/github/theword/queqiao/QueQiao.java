package com.github.theword.queqiao;

import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import com.github.theword.queqiao.command.CommandExecutor;


@Mod(modid = BaseConstant.MOD_ID, version = QueQiao.VERSION, name = BaseConstant.MODULE_NAME, acceptableRemoteVersions = "*", serverSideOnly = false, clientSideOnly = false)
public class QueQiao {
    public static final String VERSION = Tags.VERSION;
    public static MinecraftServer minecraftServer;

    /**
     * Runtime 在服务端启动后才存在；事件与命令通过供应商延迟取值。
     */
    private static volatile QueQiaoRuntime runtime;

    private static volatile PlatformContext platformContext;

    public QueQiao() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventProcessor(QueQiao::getPlatformContext));
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();
        event.registerServerCommand(new CommandExecutor());
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        PlatformContext context = new PlatformContext(minecraftServer);
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        QueQiaoRuntime current = runtime;
        if (current != null) {
            current.shutdown();
        }
    }

    public static QueQiaoRuntime getRuntime() {
        return runtime;
    }

    public static PlatformContext getPlatformContext() {
        return platformContext;
    }
}
