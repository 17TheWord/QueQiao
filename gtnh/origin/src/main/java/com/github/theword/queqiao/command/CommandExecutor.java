package com.github.theword.queqiao.command;

import com.github.theword.queqiao.QueQiao;
import io.github.theword.queqiao.core.command.CommandRouter;
import io.github.theword.queqiao.core.constant.BaseConstant;
import io.github.theword.queqiao.core.constant.CommandConstant;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class CommandExecutor extends CommandBase {

    private QueQiaoRuntime cachedRuntime;

    private CommandRouter<ICommandSender> cachedRouter;

    @Override
    public String getCommandName() {
        return BaseConstant.COMMAND_HEADER;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + BaseConstant.COMMAND_HEADER;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender.canCommandSenderUseCommand(CommandConstant.MOD_PERMISSION_LEVEL, BaseConstant.COMMAND_HEADER);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        QueQiaoRuntime runtime = QueQiao.getRuntime();
        if (runtime == null) return Collections.emptyList();

        return router(runtime).tabComplete(new ForgeCommandContext(sender), args);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        QueQiaoRuntime runtime = QueQiao.getRuntime();
        if (runtime == null) {
            sender.addChatMessage(new ChatComponentText(CommandConstant.RUNTIME_NOT_READY));
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
