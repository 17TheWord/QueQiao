package com.github.theword.queqiao.command.subCommand;


import com.github.theword.queqiao.command.CommandManager;
import com.github.theword.queqiao.command.SpigotSubCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;

public class HelpCommand extends HelpCommandAbstract implements SpigotSubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!handleCommandReturnMessageService.hasPermission(commandSender, getPermissionNode())) return false;
        commandSender.sendMessage("-------------------");
        for (SubCommand subCommand : new CommandManager().getSubCommandList()) {
            commandSender.sendMessage(subCommand.getUsage() + "---" + subCommand.getDescription());
        }
        commandSender.sendMessage("-------------------");
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