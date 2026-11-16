package com.github.theword.queqiao.command;

import com.github.theword.queqiao.utils.FabricTool;
// IF > fabric-1.18.2
//import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
// ELSE
//import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
// END IF
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.theword.queqiao.core.command.CommandNode;
import io.github.theword.queqiao.core.command.RootCommand;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.function.Supplier;

public class CommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConstant.MODULE_NAME);

    public CommandExecutor(Supplier<QueQiaoRuntime> runtimeSupplier) {
        // 命令在服务端启动阶段注册，此时 Runtime 可能还不存在，因此使用延迟命令树
        RootCommand<ServerCommandSource> rootCommand =
                QueQiaoRuntime.createDeferredCommandTree(LOGGER, runtimeSupplier);

        // IF >= fabric-1.19
//        CommandRegistrationCallback.EVENT.register(
//                (dispatcher, registryAccess, environment) ->
                        // ELSE
//        CommandRegistrationCallback.EVENT.register(
//                (dispatcher, dedicated) ->
                        // END IF
                        dispatcher.register(registerCommand(rootCommand))
        );
    }

    /**
     * 把 core 的命令树递归映射为 Brigadier 的 LiteralArgumentBuilder
     *
     * @param node 当前命令节点
     * @return Brigadier 命令构建器
     */
    private static LiteralArgumentBuilder<ServerCommandSource> registerCommand(CommandNode<ServerCommandSource> node) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(node.getName());
        if (node.isRoot()) {
            builder.requires(FabricTool::permissionCheck);
        }
        builder.executes(context -> node.execute(
                new FabricCommandContext(context.getSource()), Collections.emptyList()));
        for (CommandNode<ServerCommandSource> child : node.getChildren()) {
            builder.then(registerCommand(child));
        }
        return builder;
    }
}
