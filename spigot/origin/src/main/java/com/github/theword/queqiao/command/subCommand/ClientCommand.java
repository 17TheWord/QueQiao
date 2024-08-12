package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.SpigotSubCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ClientCommand implements SpigotSubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("§c请输入子命令");
            return false;
        } else if (args[1].equalsIgnoreCase("reconnect")) {
            return new ReconnectCommand().onCommand(commandSender, args);
        }
        return false;
    }

    @Override
    public List<String> getSubCommands(CommandSender commandSender, String[] args) {
        return new ArrayList<String>() {{
            add("reconnect");
        }};
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getName() {
        return "client";
    }

    @Override
    public String getDescription() {
        return "Websocket Client 的命令";
    }

    @Override
    public String getUsage() {
        return "使用：/mcqq client <args>";
    }
}
