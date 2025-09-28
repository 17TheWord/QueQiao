package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import com.mojang.brigadier.Command;
import com.velocitypowered.api.command.SimpleCommand;


public class ReloadCommand extends ReloadCommandAbstract implements VelocitySubCommand {
    @Override
    public int onCommand(SimpleCommand.Invocation invocation) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(invocation, getPermissionNode())) return 0;
        execute(invocation, false);
        return Command.SINGLE_SUCCESS;
    }
}