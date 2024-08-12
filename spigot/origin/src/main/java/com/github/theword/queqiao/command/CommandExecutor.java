package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CommandExecutor implements TabExecutor {
    private final CommandManager commandManager = new CommandManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return new HelpCommand().onCommand(commandSender, args);
        } else {
            for (SpigotSubCommand subCommand : commandManager.getSubCommandList()) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    return subCommand.onCommand(commandSender, args);
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase(BaseConstant.COMMAND_HEADER)) {
            if (args.length == 1) {
                List<String> commandList = new ArrayList<>();
                commandManager.getSubCommandList().forEach(subCommand -> {
                    if (subCommand.getPrefix() == null) {
                        commandList.add(subCommand.getName());
                    }
                });
                return commandList;
            } else if (args.length == 2) {
                for (SpigotSubCommand subCommand : commandManager.getSubCommandList()) {
                    if (subCommand.getName().equalsIgnoreCase(args[0])) {
                        return subCommand.getSubCommands(commandSender, args);
                    }
                }
            } else if (args.length == 3) {
                for (SpigotSubCommand subCommand : commandManager.getSubCommandList()) {
                    if (subCommand.getName().equalsIgnoreCase(args[1])) {
                        return subCommand.getSubCommands(commandSender, args);
                    }
                }
            }
        }
        return new ArrayList<>();
    }
}
