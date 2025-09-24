package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.SubCommand;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public interface VelocitySubCommand extends SubCommand {

    int onCommand(SimpleCommand.Invocation invocation);

}