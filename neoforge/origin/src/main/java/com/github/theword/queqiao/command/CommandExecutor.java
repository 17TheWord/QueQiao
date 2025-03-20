package com.github.theword.queqiao.command;


import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectAllCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;

@EventBusSubscriber(modid = BaseConstant.MOD_ID)
public class CommandExecutor {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal(BaseConstant.COMMAND_HEADER)
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> new HelpCommand().onCommand(context))
                        .then(Commands.literal("help")
                                .executes(context -> new HelpCommand().onCommand(context))
                        )
                        .then(Commands.literal("reload")
                                .executes(context -> new ReloadCommand().onCommand(context))
                        )
                        .then(Commands.literal("client")
                                .then(Commands.literal("reconnect")
                                        .executes(context -> new ReconnectCommand().onCommand(context))
                                        .then(Commands.literal("all")
                                                .executes(context -> new ReconnectAllCommand().onCommand(context)))
                                )
                        ).then(Commands.literal("server")
                                .executes(context -> {
                                    // TODO Websocket Server Command
                                    handleCommandReturnMessageService.handleCommandReturnMessage(context, "Server command is not supported");
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
        );
        ConfigCommand.register(event.getDispatcher());
    }

}
