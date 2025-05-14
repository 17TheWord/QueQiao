package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.NeoForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class ReconnectCommand extends ReconnectCommandAbstract implements NeoForgeSubCommand {

    @Override
    public int onCommand(CommandContext<net.minecraft.commands.CommandSourceStack> context) {
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        execute(context, false);
        return Command.SINGLE_SUCCESS;
    }

}