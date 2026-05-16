package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.github.theword.queqiao.utils.NeoForgeTool;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        CommandContext<CommandSourceStack> context = (CommandContext<CommandSourceStack>) object;
        context.getSource().sendSystemMessage(MutableComponent.create(new PlainTextContents.LiteralContents(message)));
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
        CommandContext<CommandSourceStack> context = (CommandContext<CommandSourceStack>) object;
        try {
            return NeoForgeTool.checkPermission(context.getSource().getPlayerOrException());
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}