package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessage;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class HandleCommandReturnMessageService implements HandleCommandReturnMessage {

    /**
     * @param object  命令返回者
     * @param message 返回消息
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        // IF fabric-1.20.1
// CommandContext<ServerCommandSource> context = (CommandContext<ServerCommandSource>) object;
// context.getSource().sendFeedback(() -> Text.literal(message), false);
        // ELSE IF fabric-1.19.2
// CommandContext<ServerCommandSource> context = (CommandContext<ServerCommandSource>) object;
// context.getSource().sendFeedback(Text.literal(message), false);
        // ELSE IF fabric-1.18.2
//
        // END IF
    }
}