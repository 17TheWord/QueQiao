package com.github.theword.queqiao.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public interface FabricSubCommand extends SubCommand {
    String getName();

    String getDescription();

    String getUsage();

    int onCommand(CommandContext<ServerCommandSource> context);
}