package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;

public class HelpCommand extends HelpCommandAbstract implements VelocitySubCommand {

    @Override
    public int onCommand(CommandContext<CommandSource> context) {
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        for (VelocitySubCommand forgeSubCommand : new CommandManager().getSubCommandList()) {
            handleCommandReturnMessageService.handleCommandReturnMessage(context, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;
    }
}