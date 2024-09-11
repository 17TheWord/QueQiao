package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;


public class ReloadCommand extends ReloadCommandAbstract implements ForgeSubCommand {

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