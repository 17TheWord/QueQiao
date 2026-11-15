package com.github.theword.queqiao;

import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(BaseConstant.MOD_ID)
public class QueQiao {

    /**
     * Runtime 在服务端启动后才存在；事件与命令通过供应商延迟取值。
     */
    private static volatile QueQiaoRuntime runtime;

    private static volatile PlatformContext platformContext;

    public QueQiao() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventProcessor(QueQiao::getPlatformContext));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onServerStarted(net.minecraftforge.event.server.ServerStartedEvent event) {
        PlatformContext context = new PlatformContext(event.getServer());
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onServerStopping(net.minecraftforge.event.server.ServerStoppingEvent event) {
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
