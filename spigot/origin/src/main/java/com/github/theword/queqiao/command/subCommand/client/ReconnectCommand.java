package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.SpigotSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


public class ReconnectCommand extends ReconnectCommandAbstract implements SpigotSubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("reconnect")) {
                execute(commandSender, false);
                return true;
            }
        } else if (args.length == 3) {
            if (args[2].equalsIgnoreCase("all")) {
                execute(commandSender, true);
                return true;
            }
        }
        commandSender.sendMessage(getUsage());
        return false;
    }

    @Override
    public List<String> getSubCommands(CommandSender commandSender, String[] args) {
        return new ArrayList<String>() {{
            add("all");
        }};
    }

    @Override
    public String getPrefix() {
        return "client";
    }
}