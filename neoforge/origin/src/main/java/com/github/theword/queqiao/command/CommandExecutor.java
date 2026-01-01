package com.github.theword.queqiao.command;


import com.github.theword.queqiao.tool.command.RootCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

import java.util.Collections;


@EventBusSubscriber(modid = BaseConstant.MOD_ID)
public class CommandExecutor {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        RootCommand rootCommand = new RootCommand();
        event.getDispatcher().register(registerSubCommand(rootCommand));
        ConfigCommand.register(event.getDispatcher());
    }


    /**
     * 递归将 SubCommand 转换为 Brigadier 的 LiteralArgumentBuilder
     *
     * @param command 当前命令节点
     * @return Brigadier 命令构建器
     */
    private static LiteralArgumentBuilder<CommandSourceStack> registerSubCommand(SubCommand command) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(command.getName());
        if (command.isRoot()) {
            builder.requires(commandSourceStack -> commandSourceStack.hasPermission(BaseConstant.MOD_PERMISSION_LEVEL));
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