package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.contents.PlainTextContents;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        CommandContext<net.minecraft.commands.CommandSourceStack> context = (CommandContext<net.minecraft.commands.CommandSourceStack>) object;
        context.getSource().sendSystemMessage(net.minecraft.network.chat.MutableComponent.create(new PlainTextContents.LiteralContents(message)));
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
        CommandContext<net.minecraft.commands.CommandSourceStack> context = (CommandContext<net.minecraft.commands.CommandSourceStack>) object;
        if (context.getSource().hasPermission(BaseConstant.MOD_PERMISSION_LEVEL)) return true;
        handleCommandReturnMessage(object, "您没有权限执行此命令");
        return false;
    }
}