package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessage;
import org.bukkit.command.CommandSender;

public class HandleCommandReturnMessageService implements HandleCommandReturnMessage {
    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        CommandSender commandSender = (CommandSender) object;
        commandSender.sendMessage(message);
    }
}