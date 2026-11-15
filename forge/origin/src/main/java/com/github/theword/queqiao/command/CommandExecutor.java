package com.github.theword.queqiao.command;

import com.github.theword.queqiao.QueQiao;
import com.github.theword.queqiao.utils.ForgeTool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.theword.queqiao.core.command.CommandNode;
import io.github.theword.queqiao.core.command.RootCommand;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Mod.EventBusSubscriber(modid = BaseConstant.MOD_ID)
public class CommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConstant.MODULE_NAME);

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
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
            builder.requires(ForgeTool::permissionCheck);
        }
        builder.executes(context ->
                node.execute(new ForgeCommandContext(context.getSource()), Collections.emptyList()));
        for (CommandNode<CommandSourceStack> child : node.getChildren()) {
            builder.then(register(child));
        }
        return builder;
    }
}
