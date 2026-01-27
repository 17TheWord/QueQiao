package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.CommandExecutorHelper;
import com.github.theword.queqiao.tool.constant.CommandConstant;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class CommandExecutor implements TabExecutor {
    private final CommandExecutorHelper commandExecutorHelper;

    public CommandExecutor() {
        this.commandExecutorHelper = new CommandExecutorHelper();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int execute = commandExecutorHelper.execute(commandSender, args);
        return execute == CommandConstant.SUCCESS_SIGNAL;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return commandExecutorHelper.tabComplete(commandSender, args);
    }
}