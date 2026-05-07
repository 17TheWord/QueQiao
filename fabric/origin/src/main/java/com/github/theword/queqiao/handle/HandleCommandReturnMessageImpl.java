package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static com.github.theword.queqiao.utils.FabricTool.permissionCheck;

public class HandleCommandReturnMessageImpl extends HandleCommandReturnMessageService {

    /**
     * @param object  命令返回者
     * @param message 返回消息
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        CommandContext<CommandSourceStack> context = (CommandContext<CommandSourceStack>) object;
        if (context.getSource() != null)
            context.getSource().sendSystemMessage(Component.literal(message));
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
        return permissionCheck(context.getSource());
    }
}