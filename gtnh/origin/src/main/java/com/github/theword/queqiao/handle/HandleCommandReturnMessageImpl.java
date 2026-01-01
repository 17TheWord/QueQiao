package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.CommandConstant;
import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        ICommandSender sender = (ICommandSender) object;
        sender.addChatMessage(new ChatComponentText(message));
    }

    @Override
    public boolean hasPermission(Object object, String node) {
        ICommandSender sender = (ICommandSender) object;
        return sender.canCommandSenderUseCommand(CommandConstant.MOD_PERMISSION_LEVEL, BaseConstant.COMMAND_HEADER);
    }
}