package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectAllCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.mojang.brigadier.Command;
// IF > forge-1.16.5
//import net.minecraft.commands.Commands;
// ELSE
//import net.minecraft.command.Commands;
// END IF
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


@Mod.EventBusSubscriber(modid = BaseConstant.MOD_ID)
public class CommandExecutor {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
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