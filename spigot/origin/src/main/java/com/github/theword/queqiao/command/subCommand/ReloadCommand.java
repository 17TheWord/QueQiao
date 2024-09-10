package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.SpigotSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class ReloadCommand extends ReloadCommandAbstract implements SpigotSubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!handleCommandReturnMessageService.hasPermission(commandSender, getPermissionNode())) return false;
        execute(commandSender, false);
        return true;
    }

    @Override
    public List<String> getSubCommands(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPrefix() {
        return null;
    }
}