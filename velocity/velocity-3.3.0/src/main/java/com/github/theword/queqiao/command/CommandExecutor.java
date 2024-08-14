package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectAllCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

public class CommandExecutor {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder(BaseConstant.COMMAND_HEADER)
                .requires(source -> source.hasPermission("queqiao.admin"))
                .executes(context -> new HelpCommand().onCommand(context))
                .then(LiteralArgumentBuilder.<CommandSource>literal("help")
                        .executes(context -> new HelpCommand().onCommand(context)))
                .then(LiteralArgumentBuilder.<CommandSource>literal("reload")
                        .executes(context -> new ReloadCommand().onCommand(context)))
                .then(LiteralArgumentBuilder.<CommandSource>literal("server")
                        .executes(context -> {
                            context.getSource().sendMessage(Component.text("Server command not supported."));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(LiteralArgumentBuilder.<CommandSource>literal("client")
                        .then(LiteralArgumentBuilder.<CommandSource>literal("reconnect")
                                .executes(context -> new ReconnectCommand().onCommand(context))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("all")
                                        .executes(context -> new ReconnectAllCommand().onCommand(context)
                                        )
                                )
                        )
                ).build();
        return new BrigadierCommand(helloNode);
    }
}
