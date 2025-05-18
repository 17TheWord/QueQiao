package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {

    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
//        CommandContext<net.minecraft.command.CommandSource> context = (CommandContext<net.minecraft.command.CommandSource>) object;
//        context.getSource().sendSuccess(new net.minecraft.util.text.StringTextComponent(message), false);
    }

    /**
     * 判断发送者是否有权执行命令
     * <p>MOD端中无权限节点，权限等级为2</p>
     *
     * @param object 命令返回者
     * @param node   权限节点
     * @return boolean 是否有权限
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean hasPermission(Object object, String node) {
//        CommandContext<net.minecraft.command.CommandSource> context = (CommandContext<net.minecraft.command.CommandSource>) object;
//        if (context.getSource().hasPermission(BaseConstant.MOD_PERMISSION_LEVEL)) return true;
//        handleCommandReturnMessage(object, "您没有权限执行此命令");
        return false;
    }
}