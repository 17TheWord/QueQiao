package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.NeoForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class HelpCommand extends HelpCommandAbstract implements NeoForgeSubCommand {

    @Override
    public int onCommand(CommandContext<net.minecraft.commands.CommandSourceStack> context) {
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        for (NeoForgeSubCommand forgeSubCommand : new CommandManager().getSubCommandList()) {
            handleCommandReturnMessageService.handleCommandReturnMessage(context, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        handleCommandReturnMessageService.handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;

    }
}