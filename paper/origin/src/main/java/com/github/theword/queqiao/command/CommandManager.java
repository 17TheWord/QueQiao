package com.github.theword.queqiao.command;


import com.github.theword.queqiao.command.subCommand.ClientCommand;
import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.ServerCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    List<PaperSubCommand> subCommandList = new ArrayList<>();


    public CommandManager() {
        subCommandList.add(new HelpCommand());
        subCommandList.add(new ReloadCommand());
        subCommandList.add(new ClientCommand());
        subCommandList.add(new ServerCommand());

        subCommandList.add(new ReconnectCommand());
    }

    public List<PaperSubCommand> getSubCommandList() {
        return subCommandList;
    }
}