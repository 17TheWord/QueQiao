package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandExecutionContext;
import io.github.theword.queqiao.core.constant.CommandConstant;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

/**
 * 一次命令调用的执行上下文（NeoForge 版）。
 */
public final class NeoForgeCommandContext extends CommandExecutionContext<CommandSourceStack> {

    public NeoForgeCommandContext(CommandSourceStack nativeSource) {
        super(nativeSource);
    }

    @Override
    public void reply(String message) {
        nativeSource.sendSystemMessage(Component.literal(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        // MOD 端无权限节点，统一按权限等级判定
        return nativeSource.hasPermission(CommandConstant.MOD_PERMISSION_LEVEL);
    }
}
