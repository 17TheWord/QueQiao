package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {
    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object o, String s) {
        CommandContext<CommandSource> context = (CommandContext<CommandSource>) o;
        context.getSource().sendMessage(Component.text(s));
    }

    /**
     * @param object CommandContext
     * @param node   权限节点
     * @return 是否有权限
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean hasPermission(Object object, String node) {
        CommandContext<CommandSource> context = (CommandContext<CommandSource>) object;
        if (context.getSource().hasPermission(node)) return true;
        handleCommandReturnMessage(object, "您没有执行此命令的权限");
        return false;
    }
}