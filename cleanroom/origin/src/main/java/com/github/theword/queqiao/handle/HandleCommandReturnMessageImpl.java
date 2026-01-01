package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.CommandConstant;
import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    @Override
    public void handleCommandReturnMessage(Object object, String message) {
        ICommandSender sender = (ICommandSender) object;
        sender.sendMessage(new TextComponentString(message));
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
    public boolean hasPermission(Object object, String node) {
        ICommandSender sender = (ICommandSender) object;
        return sender.canUseCommand(CommandConstant.MOD_PERMISSION_LEVEL, BaseConstant.COMMAND_HEADER);
    }
}