package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {

    /**
     * @param object  命令返回者
     * @param message 返回消息
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        CommandContext<ServerCommandSource> context = (CommandContext<ServerCommandSource>) object;
        if (context.getSource().getEntity() instanceof ServerPlayerEntity)
            // IF >= fabric-1.20
//            context.getSource().sendFeedback(() -> Text.of(message), false);
        // ELSE
//            context.getSource().sendFeedback(Text.of(message), false);
        // END IF
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
        CommandContext<ServerCommandSource> context = (CommandContext<ServerCommandSource>) object;
        if (context.getSource().hasPermissionLevel(BaseConstant.MOD_PERMISSION_LEVEL)) return true;
        handleCommandReturnMessage(object, "你没有权限执行此命令");
        return false;
    }
}