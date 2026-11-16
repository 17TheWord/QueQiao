package com.github.theword.queqiao.command;

import com.github.theword.queqiao.QueQiao;
import io.github.theword.queqiao.core.command.CommandRouter;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.constant.CommandConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandExecutor extends CommandBase {

    private QueQiaoRuntime cachedRuntime;

    private CommandRouter<ICommandSender> cachedRouter;

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return CommandConstant.MOD_PERMISSION_LEVEL;
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     *
     * @param server 服务器
     * @param sender 发送者
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(getRequiredPermissionLevel(), getName());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        QueQiaoRuntime runtime = QueQiao.getRuntime();
        if (runtime == null) return Collections.emptyList();

        return router(runtime).tabComplete(new ForgeCommandContext(sender), args);
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return BaseConstant.COMMAND_HEADER;
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender 发送者
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + BaseConstant.COMMAND_HEADER;
    }

    /**
     * Callback for when the command is executed
     *
     * @param server 服务器
     * @param sender 发送者
     * @param args   参数
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        QueQiaoRuntime runtime = QueQiao.getRuntime();
        if (runtime == null) {
            sender.sendMessage(new TextComponentString(CommandConstant.RUNTIME_NOT_READY));
            return;
        }

        router(runtime).execute(new ForgeCommandContext(sender), args);
    }

    /**
     * 命令路由要求 Runtime 已启动，因此按当前 Runtime 实例惰性创建并缓存。
     */
    private CommandRouter<ICommandSender> router(QueQiaoRuntime runtime) {
        CommandRouter<ICommandSender> current = cachedRouter;
        if (current != null && cachedRuntime == runtime) {
            return current;
        }

        CommandRouter<ICommandSender> created = runtime.createCommandRouter();
        cachedRuntime = runtime;
        cachedRouter = created;
        return created;
    }
}
