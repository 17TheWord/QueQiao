package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {

    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        // CommandContext<net.minecraft.command.CommandSource> context =
        // (CommandContext<net.minecraft.command.CommandSource>) object;
        // context.getSource().sendSuccess(new net.minecraft.util.text.StringTextComponent(message), false);
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
