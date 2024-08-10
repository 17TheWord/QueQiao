package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.FabricSubCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessage;


public class HelpCommand extends HelpCommandAbstract implements FabricSubCommand {


    @Override
    public int onCommand(CommandContext<ServerCommandSource> context) {
        handleCommandReturnMessage.handleCommandReturnMessage(context, "-------------------");
        for (SubCommand subCommand : new CommandManager().getSubCommandList()) {
            handleCommandReturnMessage.handleCommandReturnMessage(context, subCommand.getUsage() + "---" + subCommand.getDescription());
        }
        handleCommandReturnMessage.handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;
    }
}