package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.FabricSubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;


public class ReconnectCommand extends ReconnectCommandAbstract implements FabricSubCommand {

    /**
     * @param context context
     * @return int
     */
    @Override
    public int onCommand(CommandContext<ServerCommandSource> context) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(context, getPermissionNode())) return 0;
        execute(context, false);
        return Command.SINGLE_SUCCESS;
    }
}