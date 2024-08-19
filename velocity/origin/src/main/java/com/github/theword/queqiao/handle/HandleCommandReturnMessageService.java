package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessage;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

public class HandleCommandReturnMessageService implements HandleCommandReturnMessage {
    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object o, String s) {
        CommandContext<CommandSource> context = (CommandContext<CommandSource>) o;
        context.getSource().sendMessage(Component.text(s));
    }
}
