package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessage;

public class HelpCommand extends HelpCommandAbstract implements VelocitySubCommand {


    @Override
    public int onCommand(CommandContext<CommandSource> context) {
        handleCommandReturnMessage.handleCommandReturnMessage(context, "-------------------");
        for (VelocitySubCommand forgeSubCommand : new CommandManager().getSubCommandList()) {
            handleCommandReturnMessage.handleCommandReturnMessage(context, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        handleCommandReturnMessage.handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;
    }
}
