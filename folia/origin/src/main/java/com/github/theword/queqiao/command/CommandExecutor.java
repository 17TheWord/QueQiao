package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandRouter;
import io.github.theword.queqiao.core.constant.CommandConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;


public class CommandExecutor implements TabExecutor {

    private final Supplier<QueQiaoRuntime> runtimeSupplier;

    private QueQiaoRuntime cachedRuntime;

    private CommandRouter<CommandSender> cachedRouter;

    public CommandExecutor(Supplier<QueQiaoRuntime> runtimeSupplier) {
        this.runtimeSupplier = runtimeSupplier;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        QueQiaoRuntime runtime = runtimeSupplier.get();
        if (runtime == null) return false;

        int result = router(runtime).execute(new FoliaCommandContext(commandSender), args);
        return result == CommandConstant.SUCCESS_SIGNAL;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        QueQiaoRuntime runtime = runtimeSupplier.get();
        if (runtime == null) return Collections.emptyList();

        return router(runtime).tabComplete(new FoliaCommandContext(commandSender), args);
    }

    private CommandRouter<CommandSender> router(QueQiaoRuntime runtime) {
        CommandRouter<CommandSender> current = cachedRouter;
        if (current != null && cachedRuntime == runtime) {
            return current;
        }

        CommandRouter<CommandSender> created = runtime.createCommandRouter();
        cachedRuntime = runtime;
        cachedRouter = created;
        return created;
    }
}
