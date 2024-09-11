package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.FabricSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class ReloadCommand extends ReloadCommandAbstract implements FabricSubCommand {


    /**
     * @param context ServerCommandSource
     * @return int
     */
    @Override
    public int onCommand(CommandContext<ServerCommandSource> context) {
        if (!handleCommandReturnMessageService.hasPermission(context, getPermissionNode())) return 0;
        execute(context, true);
        return Command.SINGLE_SUCCESS;
    }
}