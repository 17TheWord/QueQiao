package com.github.theword.queqiao.handle;

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
        // CommandContext<net.minecraft.command.CommandSource> context =
        // (CommandContext<net.minecraft.command.CommandSource>) object;
        // if (context.getSource().hasPermission(BaseConstant.MOD_PERMISSION_LEVEL)) return true;
        // handleCommandReturnMessage(object, "您没有权限执行此命令");
        return false;
    }
}
