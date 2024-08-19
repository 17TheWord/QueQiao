package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.SubCommand;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

public interface VelocitySubCommand extends SubCommand {

    int onCommand(CommandContext<CommandSource> context);

}
