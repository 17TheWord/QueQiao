package com.github.theword.queqiao.command;


import com.github.theword.queqiao.QueQiao;
import com.github.theword.queqiao.utils.NeoForgeTool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.theword.queqiao.core.command.CommandNode;
import io.github.theword.queqiao.core.command.RootCommand;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;


@EventBusSubscriber(modid = BaseConstant.MOD_ID)
public class CommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConstant.MODULE_NAME);

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        // 命令注册发生在服务端启动阶段，此时 Runtime 可能还不存在，因此使用延迟命令树
        RootCommand<CommandSourceStack> rootCommand =
                QueQiaoRuntime.createDeferredCommandTree(LOGGER, QueQiao::getRuntime);

        event.getDispatcher().register(register(rootCommand));
        ConfigCommand.register(event.getDispatcher());
    }

    /**
     * 递归将 Core 命令节点转换为 Brigadier 的 LiteralArgumentBuilder
     *
     * @param node 当前命令节点
     * @return Brigadier 命令构建器
     */
    private static LiteralArgumentBuilder<CommandSourceStack> register(CommandNode<CommandSourceStack> node) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(node.getName());
        if (node.isRoot()) {
            builder.requires(commandSourceStack -> {
                try {
                    return NeoForgeTool.checkPermission(commandSourceStack.getPlayerOrException());
                } catch (CommandSyntaxException e) {
                    return false;
                }
            });
        }
        builder.executes(context ->
                node.execute(new NeoForgeCommandContext(context.getSource()), Collections.emptyList()));
        for (CommandNode<CommandSourceStack> child : node.getChildren()) {
            builder.then(register(child));
        }
        return builder;
    }
}
