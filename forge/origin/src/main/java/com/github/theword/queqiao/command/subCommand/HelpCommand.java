package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class HelpCommand extends HelpCommandAbstract implements ForgeSubCommand {

    @Override
    // IF > forge-1.16.5
//    public int onCommand(CommandContext<net.minecraft.commands.CommandSourceStack> context) {
    // ELSE
//    public int onCommand(CommandContext<net.minecraft.command.CommandSource> context) {
        // END IF
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        for (ForgeSubCommand forgeSubCommand : new CommandManager().getSubCommandList()) {
            handleCommandReturnMessageService.handleCommandReturnMessage(context, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;

    }
}