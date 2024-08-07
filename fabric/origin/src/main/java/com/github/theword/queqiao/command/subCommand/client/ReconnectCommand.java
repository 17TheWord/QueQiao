package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.FabricSubCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import static com.github.theword.queqiao.utils.Tool.websocketManager;


public class ReconnectCommand extends ReconnectCommandAbstract implements FabricSubCommand {

    /**
     * @param context context
     * @return int
     */
    @Override
    public int onCommand(CommandContext<ServerCommandSource> context) {
        websocketManager.reconnectWebsocketClients(false, context);
        return Command.SINGLE_SUCCESS;
    }
}