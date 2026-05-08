package com.github.theword.queqiao.handle

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService
import org.bukkit.command.CommandSender


class HandleCommandReturnMessageImpl : HandleCommandReturnMessageService() {
    /**
     * 处理命令返回消息
     *
     * @param sender  命令发送者
     * @param message 消息
     */
    override fun handleCommandReturnMessage(sender: Any?, message: String) {
        val commandSender = sender as CommandSender
        commandSender.sendMessage(message)
    }

    /**
     * 判断命令发送者是否有权限执行命令
     *
     * @param sender 命令发送者
     * @param node   权限节点
     * @return 是否有权限
     */
    override fun hasPermission(sender: Any?, node: String): Boolean {
        val commandSender = sender as CommandSender
        return commandSender.hasPermission(node)
    }
}