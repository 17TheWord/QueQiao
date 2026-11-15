package com.github.theword.queqiao.command;

import com.github.theword.queqiao.utils.NeoForgeTool;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.theword.queqiao.core.command.CommandExecutionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

/**
 * 一次命令调用的执行上下文（Brigadier 版）。
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
        // MOD 端无权限节点，统一按权限等级 2（OP）判定
        try {
            return NeoForgeTool.checkPermission(nativeSource.getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}
