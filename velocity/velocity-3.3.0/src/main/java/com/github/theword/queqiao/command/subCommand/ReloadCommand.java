package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

public class ReloadCommand extends ReloadCommandAbstract implements VelocitySubCommand {
    @Override
    public int onCommand(CommandContext<CommandSource> context) {
        websocketManager.reloadWebsocket(true, context);
        return Command.SINGLE_SUCCESS;
    }
}
