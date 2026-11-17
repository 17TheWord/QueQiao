package com.github.theword.queqiao;

import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;


@Mod(BaseConstant.MOD_ID)
public class QueQiao {

    /**
     * Runtime 在服务端启动后才存在；事件与命令通过供应商延迟取值。
     */
    private static volatile QueQiaoRuntime runtime;

    private static volatile PlatformContext platformContext;

    public QueQiao() {
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new EventProcessor(QueQiao::getPlatformContext));
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        PlatformContext context = new PlatformContext(event.getServer());
        QueQiaoRuntime created = QueQiaoRuntime.create(context);
        context.bindRuntime(created);
        created.start();

        platformContext = context;
        runtime = created;
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
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
