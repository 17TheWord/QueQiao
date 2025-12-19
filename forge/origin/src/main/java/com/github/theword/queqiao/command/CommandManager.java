package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<ForgeSubCommand> getSubCommandList() {
        return subCommandList;
    }

    private final List<ForgeSubCommand> subCommandList = new ArrayList<>();

    public CommandManager() {
        subCommandList.add(new HelpCommand());
        subCommandList.add(new ReloadCommand());
        subCommandList.add(new ReconnectCommand());
    }

}