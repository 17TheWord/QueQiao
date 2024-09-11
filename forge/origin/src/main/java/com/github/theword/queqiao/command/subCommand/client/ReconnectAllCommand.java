package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class ReconnectAllCommand extends ReconnectCommandAbstract implements ForgeSubCommand {

    @Override
    // IF > forge-1.16.5
//    public int onCommand(CommandContext<net.minecraft.commands.CommandSourceStack> context) {
        // ELSE
//    public int onCommand(CommandContext<net.minecraft.command.CommandSource> context) {
        // END IF
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        execute(context, true);
        return Command.SINGLE_SUCCESS;
    }

}