package com.github.theword.queqiao.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public interface NeoForgeSubCommand {

    String getName();
    String getDescription();
    String getUsage();

    int onCommand(CommandContext<CommandSourceStack> context);
}
