package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;


public class ReconnectCommand extends ReconnectCommandAbstract implements VelocitySubCommand {
    @Override
    public int onCommand(CommandContext<CommandSource> context) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(context, getPermissionNode())) return 0;
        execute(context, false);
        return Command.SINGLE_SUCCESS;
    }
}