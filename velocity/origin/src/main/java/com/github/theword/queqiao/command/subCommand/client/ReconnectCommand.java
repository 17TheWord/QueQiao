package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.VelocitySubCommand;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

public class ReconnectCommand extends ReconnectCommandAbstract implements VelocitySubCommand {
    @Override
    public int onCommand(CommandContext<CommandSource> context) {
        websocketManager.reconnectWebsocketClients(false, context);
        return Command.SINGLE_SUCCESS;
    }
}
