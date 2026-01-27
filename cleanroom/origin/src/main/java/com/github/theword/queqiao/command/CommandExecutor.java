package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.CommandExecutorHelper;
import com.github.theword.queqiao.tool.constant.CommandConstant;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandExecutor extends CommandBase {

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
        return commandExecutorHelper.tabComplete(sender, args);
    }

    private final CommandExecutorHelper commandExecutorHelper;

    public CommandExecutor() {
        commandExecutorHelper = new CommandExecutorHelper();
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return commandExecutorHelper.getRootCommand().getName();
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender 发送者
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return commandExecutorHelper.getRootCommand().getUsage();
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
        commandExecutorHelper.execute(sender, args);
    }
}