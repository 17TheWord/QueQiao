package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.FabricSubCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class HelpCommand extends HelpCommandAbstract implements FabricSubCommand {


    @Override
    public int onCommand(CommandContext<ServerCommandSource> context) {
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        for (SubCommand subCommand : new CommandManager().getSubCommandList()) {
            handleCommandReturnMessageService.handleCommandReturnMessage(context, subCommand.getUsage() + "---" + subCommand.getDescription());
        }
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;
    }
}