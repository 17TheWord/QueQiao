package com.github.theword.queqiao.command;

import com.velocitypowered.api.command.CommandSource;
import io.github.theword.queqiao.core.command.CommandExecutionContext;
import net.kyori.adventure.text.Component;

/**
 * 一次命令调用的执行上下文（Velocity 版）。
 */
public final class VelocityCommandContext extends CommandExecutionContext<CommandSource> {

    public VelocityCommandContext(CommandSource nativeSource) {
        super(nativeSource);
    }

    @Override
    public void reply(String message) {
        nativeSource.sendMessage(Component.text(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return nativeSource.hasPermission(permission);
    }
}
