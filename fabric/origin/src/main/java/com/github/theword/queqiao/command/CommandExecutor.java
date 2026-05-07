package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.RootCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.utils.FabricTool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Collections;

public class CommandExecutor {

    public CommandExecutor() {
        RootCommand rootCommand = new RootCommand();
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        dispatcher.register(registerSubCommand(rootCommand))
        );
    }

    /**
     * 递归将 SubCommand 转换为 Brigadier 的 LiteralArgumentBuilder
     *
     * @param command 当前命令节点
     * @return Brigadier 命令构建器
     */
    private LiteralArgumentBuilder<CommandSourceStack> registerSubCommand(SubCommand command) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(command.getName());
        if (command.isRoot()) {
            builder.requires(FabricTool::permissionCheck);
        }
        builder.executes(context -> command.execute(context, Collections.emptyList()));
        for (SubCommand child : command.getChildren()) {
            builder.then(registerSubCommand(child));
        }
        return builder;
    }

}