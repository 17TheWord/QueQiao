package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HandleCommandReturnMessageService implements HandleCommandReturnMessage {
    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        CommandSender commandSender = (CommandSender) object;
        if (commandSender instanceof Player)
            commandSender.sendMessage(message);
    }
}
