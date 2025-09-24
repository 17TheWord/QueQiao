package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {
    @Override
    public void handleCommandReturnMessage(Object o, String s) {
        SimpleCommand.Invocation invocation = (SimpleCommand.Invocation) o;
        invocation.source().sendMessage(Component.text(s));
    }

    /**
     * @param object CommandContext
     * @param node   权限节点
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(Object object, String node) {
        SimpleCommand.Invocation invocation = (SimpleCommand.Invocation) object;
        if (invocation.source().hasPermission(node)) return true;
        handleCommandReturnMessage(object, "您没有执行此命令的权限");
        return false;
    }
}