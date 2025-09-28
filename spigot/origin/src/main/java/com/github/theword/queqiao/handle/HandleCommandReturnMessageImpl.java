package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import org.bukkit.command.CommandSender;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    /**
     * 处理命令返回消息
     *
     * @param object  命令发送者
     * @param message 消息
     */
    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        CommandSender commandSender = (CommandSender) object;
        commandSender.sendMessage(message);
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
        CommandSender commandSender = (CommandSender) object;
        if (commandSender.hasPermission(node)) return true;
        commandSender.sendMessage("您没有权限执行当前命令");
        return false;
    }
}