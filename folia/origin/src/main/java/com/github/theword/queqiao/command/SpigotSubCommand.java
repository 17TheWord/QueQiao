package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SpigotSubCommand extends SubCommand {

    boolean onCommand(CommandSender commandSender, String[] args);

    List<String> getSubCommands(CommandSender commandSender, String[] args);

    String getPrefix();

}