package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandExecutionContext;
import org.bukkit.command.CommandSender;

/**
 * 一次命令调用的执行上下文（Bukkit 版）。
 */
public final class SpigotCommandContext extends CommandExecutionContext<CommandSender> {

    public SpigotCommandContext(CommandSender nativeSource) {
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
