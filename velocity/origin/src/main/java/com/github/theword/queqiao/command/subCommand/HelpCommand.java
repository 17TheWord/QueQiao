package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.velocitypowered.api.command.SimpleCommand;


public class HelpCommand extends HelpCommandAbstract implements VelocitySubCommand {

    @Override
    public int onCommand(SimpleCommand.Invocation invocation) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(invocation, getPermissionNode())) return 0;
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(invocation, "-------------------");
        for (VelocitySubCommand forgeSubCommand : new CommandManager().getSubCommandList()) {
            GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(invocation, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(invocation, "-------------------");
        return Command.SINGLE_SUCCESS;
    }
}