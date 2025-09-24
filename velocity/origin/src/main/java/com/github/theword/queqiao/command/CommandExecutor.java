package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandExecutor implements SimpleCommand {
    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return SimpleCommand.super.suggestAsync(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }

    private VelocitySubCommand findCommand(String[] args) {
        List<VelocitySubCommand> subCommands = new CommandManager().getSubCommandList();
        int index = 0;
        StringBuilder prefixBuilder = new StringBuilder();
        VelocitySubCommand found = null;
        while (index < args.length) {
            if (index > 0) prefixBuilder.append("/");
            prefixBuilder.append(args[index]);
            String prefix = prefixBuilder.toString();
            boolean matched = false;
            for (VelocitySubCommand cmd : subCommands) {
                if (cmd.getPrefix().equalsIgnoreCase(prefix) || cmd.getName().equalsIgnoreCase(args[index])) {
                    found = cmd;
                    matched = true;
                    break;
                }
            }
            if (!matched) break;
            index++;
        }
        return found;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission("queqiao.admin")) {
            source.sendMessage(Component.text("你没有权限执行此命令"));
            return;
        }
        if (args.length == 0) {
            new HelpCommand().onCommand(invocation);
            return;
        }
        VelocitySubCommand found = findCommand(args);
        if (found != null) {
            found.onCommand(invocation);
        } else {
            source.sendMessage(Component.text("未知子命令: " + args[0]));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        List<VelocitySubCommand> subCommands = new CommandManager().getSubCommandList();
        int index = 0;
        StringBuilder prefixBuilder = new StringBuilder();
        while (index < args.length - 1) {
            if (index > 0) prefixBuilder.append("/");
            prefixBuilder.append(args[index]);
            String prefix = prefixBuilder.toString();
            boolean matched = false;
            for (VelocitySubCommand cmd : subCommands) {
                if (cmd.getPrefix().equalsIgnoreCase(prefix) || cmd.getName().equalsIgnoreCase(args[index])) {
                    matched = true;
                    break;
                }
            }
            if (!matched) break;
            index++;
        }
        // 返回当前层所有可选命令名
        List<String> suggestions = new java.util.ArrayList<>();
        for (VelocitySubCommand cmd : subCommands) {
            if (cmd.getPrefix().equalsIgnoreCase(prefixBuilder.toString()) || index == 0) {
                suggestions.add(cmd.getName());
            }
        }
        return suggestions;
    }
}