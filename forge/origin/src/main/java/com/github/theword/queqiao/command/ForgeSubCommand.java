package com.github.theword.queqiao.command;

import com.mojang.brigadier.context.CommandContext;

public interface ForgeSubCommand {

    String getName();

    String getDescription();

    String getUsage();

    // IF > forge-1.16.5
//    int onCommand(CommandContext<net.minecraft.commands.CommandSourceStack> context);
    // ELSE
//    int onCommand(CommandContext<net.minecraft.command.CommandSource> context);
    // END IF
}