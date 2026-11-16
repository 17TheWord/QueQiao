package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandExecutionContext;
import org.bukkit.command.CommandSender;

/**
 * 一次命令调用的执行上下文（Folia 版）。
 */
public final class FoliaCommandContext extends CommandExecutionContext<CommandSender> {

    public FoliaCommandContext(CommandSender nativeSource) {
        super(nativeSource);
    }

    @Override
    public void reply(String message) {
        nativeSource.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return nativeSource.hasPermission(permission);
    }
}
