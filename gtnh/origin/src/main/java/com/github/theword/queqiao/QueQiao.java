package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

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

    /**
     * Runtime 在服务端启动后才存在；事件与命令通过供应商延迟取值。
     */
    private static volatile QueQiaoRuntime runtime;

    private static volatile PlatformContext platformContext;

    private final EventProcessor eventProcessor = new EventProcessor(QueQiao::getPlatformContext);

    public QueQiao() {
        MinecraftForge.EVENT_BUS.register(eventProcessor);
        FMLCommonHandler.instance().bus().register(eventProcessor);
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStarting(FMLServerStartingEvent event) {
        minecraftServer = event.getServer();
        event.registerServerCommand(new CommandExecutor());
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStarted(FMLServerStartedEvent event) {
        PlatformContext context = new PlatformContext(minecraftServer);
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    public void onServerStopping(FMLServerStoppingEvent event) {
        QueQiaoRuntime current = runtime;
        if (current != null) {
            current.shutdown();
        }
        runtime = null;
        platformContext = null;
    }

    public static QueQiaoRuntime getRuntime() {
        return runtime;
    }

    public static PlatformContext getPlatformContext() {
        return platformContext;
    }
}
