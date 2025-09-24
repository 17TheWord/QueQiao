package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;


public class HelpCommand extends HelpCommandAbstract implements ForgeSubCommand {

    @Override
    // IF > forge-1.16.5
//    public int onCommand(CommandContext<net.minecraft.commands.CommandSourceStack> context) {
    // ELSE
//    public int onCommand(CommandContext<net.minecraft.command.CommandSource> context) {
        // END IF
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(context, getPermissionNode())) return 0;
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, "-------------------");
        for (ForgeSubCommand forgeSubCommand : new CommandManager().getSubCommandList()) {
            GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, "-------------------");
        return Command.SINGLE_SUCCESS;

    }
}