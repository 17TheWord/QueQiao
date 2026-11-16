package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandExecutionContext;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.constant.CommandConstant;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * 一次命令调用的执行上下文（Forge 1.7.10 版）。
 */
public final class ForgeCommandContext extends CommandExecutionContext<ICommandSender> {

    public ForgeCommandContext(ICommandSender nativeSource) {
        super(nativeSource);
    }

    @Override
    public void reply(String message) {
        nativeSource.addChatMessage(new ChatComponentText(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        // MOD 端无权限节点，统一按权限等级判定
        return nativeSource.canCommandSenderUseCommand(CommandConstant.MOD_PERMISSION_LEVEL, BaseConstant.COMMAND_HEADER);
    }
}
