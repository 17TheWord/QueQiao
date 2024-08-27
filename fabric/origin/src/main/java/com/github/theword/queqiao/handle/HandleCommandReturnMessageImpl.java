package com.github.theword.queqiao.handle;

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
//          context.getSource().sendFeedback(() -> Text.of(message), false);
            // ELSE
//            context.getSource().sendFeedback(Text.of(message), false);
        // END IF
    }
}