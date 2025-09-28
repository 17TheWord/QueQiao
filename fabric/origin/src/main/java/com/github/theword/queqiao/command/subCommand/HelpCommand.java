package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.FabricSubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;


public class HelpCommand extends HelpCommandAbstract implements FabricSubCommand {


    @Override
    public int onCommand(CommandContext<ServerCommandSource> context) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(context, getPermissionNode())) return 0;
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, "-------------------");
        for (SubCommand subCommand : new CommandManager().getSubCommandList()) {
            GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, subCommand.getUsage() + "---" + subCommand.getDescription());
        }
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;
    }
}