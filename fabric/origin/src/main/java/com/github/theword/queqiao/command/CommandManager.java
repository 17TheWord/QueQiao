package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import com.github.theword.queqiao.tool.command.SubCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager {
    private final List<SubCommand> subCommandList = new ArrayList<>();

    public CommandManager() {
        subCommandList.add(new HelpCommand());
        subCommandList.add(new ReloadCommand());
        subCommandList.add(new ReconnectCommand());
    }
}