package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import org.bukkit.command.CommandSender;

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {
    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        CommandSender commandSender = (CommandSender) object;
        commandSender.sendMessage(message);
    }
}