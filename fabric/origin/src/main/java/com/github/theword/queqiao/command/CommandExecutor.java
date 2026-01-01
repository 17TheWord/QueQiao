package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.RootCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.utils.FabricTool;
import com.mojang.brigadier.Command;
// IF > fabric-1.18.2
//import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
// ELSE
//import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
// END IF
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;

public class CommandExecutor {

    public CommandExecutor() {
        RootCommand rootCommand = new RootCommand();
        // IF >= fabric-1.19
//        CommandRegistrationCallback.EVENT.register(
//                (dispatcher, registryAccess, environment) ->
                        // ELSE
//        CommandRegistrationCallback.EVENT.register(
//                (dispatcher, dedicated) ->
                        // END IF
                        dispatcher.register(registerSubCommand(rootCommand))
        );
    }

    /**
     * 递归将 SubCommand 转换为 Brigadier 的 LiteralArgumentBuilder
     *
     * @param command 当前命令节点
     * @return Brigadier 命令构建器
     */
    private LiteralArgumentBuilder<ServerCommandSource> registerSubCommand(SubCommand command) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(command.getName());
        if (command.isRoot()) {
            builder.requires(FabricTool::permissionCheck);
        }
        builder.executes(context -> {
            command.execute(context, Collections.emptyList());
            return Command.SINGLE_SUCCESS;
        });
        for (SubCommand child : command.getChildren()) {
            builder.then(registerSubCommand(child));
        }
        return builder;
    }

}