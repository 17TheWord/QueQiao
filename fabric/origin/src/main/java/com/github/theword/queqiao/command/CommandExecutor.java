package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectAllCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.mojang.brigadier.Command;
// IF > fabric-1.18.2
//import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
// ELSE
//import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
// END IF
import net.minecraft.server.command.CommandManager;

public class CommandExecutor {

    public CommandExecutor() {
        // IF >= fabric-1.19
//        CommandRegistrationCallback.EVENT.register(
//                (dispatcher, registryAccess, environment) ->
        // ELSE
//        CommandRegistrationCallback.EVENT.register(
//                (dispatcher, dedicated) ->
                        // END IF
                        dispatcher.register(
                                CommandManager.literal(BaseConstant.COMMAND_HEADER)
                                        .requires(source -> source.hasPermissionLevel(2))
                                        .executes(context -> new HelpCommand().onCommand(context))
                                        .then(CommandManager.literal("help")
                                                .executes(context -> new HelpCommand().onCommand(context))
                                        )
                                        .then(CommandManager.literal("reload")
                                                .executes(context -> new ReloadCommand().onCommand(context))
                                        )
                                        .then(CommandManager.literal("client")
                                                .then(CommandManager.literal("reconnect")
                                                        .executes(context -> new ReconnectCommand().onCommand(context))
                                                        .then(CommandManager.literal("all")
                                                                .executes(context -> new ReconnectAllCommand().onCommand(context))
                                                        )
                                                )
                                        )
                                        .then(CommandManager.literal("server")
                                                .executes(context -> {
                                                            // TODO Websocket Server Command
                                                            GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(context, "Server command is not supported");
                                                            return Command.SINGLE_SUCCESS;
                                                        }
                                                )
                                        )
                        )
        );
    }
}