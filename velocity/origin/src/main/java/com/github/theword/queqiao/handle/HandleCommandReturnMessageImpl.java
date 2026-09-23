package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import net.kyori.adventure.text.Component;
import com.velocitypowered.api.command.CommandSource;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    /**
     * 处理命令返回消息
     *
     * @param object  命令发送者
     * @param message 消息
     */
    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        CommandSource commandSource = (CommandSource) object;
        commandSource.sendMessage(Component.text(message));
    }

    /**
     * 判断命令发送者是否有权限执行命令
     *
     * @param object 命令发送者
     * @param node   权限节点
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(Object object, String node) {
        CommandSource commandSource = (CommandSource) object;
        return commandSource.hasPermission(node);
    }
}
