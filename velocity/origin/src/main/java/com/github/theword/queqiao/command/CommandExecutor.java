package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandRouter;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.constant.CommandConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CommandExecutor implements SimpleCommand {

    private final Supplier<QueQiaoRuntime> runtimeSupplier;

    private QueQiaoRuntime cachedRuntime;

    private CommandRouter<CommandSource> cachedRouter;

    public CommandExecutor(Supplier<QueQiaoRuntime> runtimeSupplier) {
        this.runtimeSupplier = runtimeSupplier;
    }

    @Override
    public void execute(Invocation invocation) {
        QueQiaoRuntime runtime = runtimeSupplier.get();
        if (runtime == null) {
            invocation.source().sendMessage(Component.text(CommandConstant.RUNTIME_NOT_READY));
            return;
        }
        router(runtime).execute(new VelocityCommandContext(invocation.source()), invocation.arguments());
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        QueQiaoRuntime runtime = runtimeSupplier.get();
        if (runtime == null) return Collections.emptyList();

        return router(runtime).tabComplete(new VelocityCommandContext(invocation.source()), invocation.arguments());
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(BaseConstant.COMMAND_HEADER);
    }

    /**
     * 命令路由要求 Runtime 已启动，因此按当前 Runtime 实例惰性创建并缓存。
     */
    private CommandRouter<CommandSource> router(QueQiaoRuntime runtime) {
        CommandRouter<CommandSource> current = cachedRouter;
        if (current != null && cachedRuntime == runtime) {
            return current;
        }

        CommandRouter<CommandSource> created = runtime.createCommandRouter();
        cachedRuntime = runtime;
        cachedRouter = created;
        return created;
    }
}
