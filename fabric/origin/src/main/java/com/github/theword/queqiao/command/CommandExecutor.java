package com.github.theword.queqiao.command;

import com.github.theword.queqiao.utils.FabricTool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.theword.queqiao.core.command.CommandNode;
import io.github.theword.queqiao.core.command.RootCommand;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.function.Supplier;

public class CommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConstant.MODULE_NAME);

    public CommandExecutor(Supplier<QueQiaoRuntime> runtimeSupplier) {
        // 命令在服务端启动阶段注册，此时 Runtime 可能还不存在，因此使用延迟命令树
        RootCommand<CommandSourceStack> rootCommand =
                QueQiaoRuntime.createDeferredCommandTree(LOGGER, runtimeSupplier);

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(register(rootCommand))
        );
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
            builder.requires(FabricTool::permissionCheck);
        }
        builder.executes(context ->
                node.execute(new FabricCommandContext(context.getSource()), Collections.emptyList()));
        for (CommandNode<CommandSourceStack> child : node.getChildren()) {
            builder.then(register(child));
        }
        return builder;
    }
}