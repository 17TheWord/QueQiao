package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.CommandExecutorHelper;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.List;

public class CommandExecutor implements SimpleCommand {

    private final CommandExecutorHelper commandExecutorHelper;

    public CommandExecutor() {
        this.commandExecutorHelper = new CommandExecutorHelper();
    }

    @Override
    public void execute(Invocation invocation) {
        commandExecutorHelper.execute(invocation.source(), invocation.arguments());
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return commandExecutorHelper.tabComplete(invocation.source(), invocation.arguments());
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(BaseConstant.COMMAND_HEADER);
    }

}