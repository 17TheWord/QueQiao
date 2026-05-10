package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.RootCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.utils.ForgeTool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Collections;


@Mod.EventBusSubscriber(modid = BaseConstant.MOD_ID)
public class CommandExecutor {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {

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
            builder.requires(ForgeTool::permissionCheck);
        }
        builder.executes(context -> command.execute(context, Collections.emptyList()));
        for (SubCommand child : command.getChildren()) {
            builder.then(registerSubCommand(child));
        }
        return builder;
    }


}