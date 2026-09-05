package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {
    @Override
    public void handleCommandReturnMessage(Object o, String s) {
        CommandSource source = (CommandSource) o;
        source.sendMessage(Component.text(s));
    }

    /**
     * @param object 命令来源
     * @param node   权限节点
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(Object object, String node) {
        CommandSource source = (CommandSource) object;
        if (source.hasPermission(node)) return true;
        handleCommandReturnMessage(object, "您没有执行此命令的权限");
        return false;
    }
}