package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.PaperSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.ServerCommandAbstract;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ServerCommand extends ServerCommandAbstract implements PaperSubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("Server commands are not implemented yet.");
        return false;
    }

    @Override
    public List<String> getSubCommands(CommandSender commandSender, String[] args) {
        return new ArrayList<String>() {
        };
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getName() {
        return "server";
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