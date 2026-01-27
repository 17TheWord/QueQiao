package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.CommandExecutorHelper;
import com.github.theword.queqiao.tool.constant.CommandConstant;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class CommandExecutor extends CommandBase {

    private final CommandExecutorHelper commandExecutorHelper;

    public CommandExecutor() {
        commandExecutorHelper = new CommandExecutorHelper();
    }

    @Override
    public String getCommandName() {
        return commandExecutorHelper.getRootCommand().getName();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return commandExecutorHelper.getRootCommand().getUsage();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        commandExecutorHelper.execute(sender, args);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return commandExecutorHelper.tabComplete(sender, args);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender.canCommandSenderUseCommand(CommandConstant.MOD_PERMISSION_LEVEL, getCommandName());
    }
}