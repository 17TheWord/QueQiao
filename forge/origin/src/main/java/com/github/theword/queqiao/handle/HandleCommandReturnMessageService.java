package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessage;
import com.mojang.brigadier.context.CommandContext;
// IF >= forge-1.21
//import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
// ELSE IF >= forge-1.19
//import net.minecraft.network.chat.contents.LiteralContents;
// END IF

public class HandleCommandReturnMessageService implements HandleCommandReturnMessage {

    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        // IF > forge-1.16.5
//        CommandContext<net.minecraft.commands.CommandSourceStack> context = (CommandContext<net.minecraft.commands.CommandSourceStack>) object;
        // END IF
        // IF >= forge-1.19
//        context.getSource().sendSystemMessage(net.minecraft.network.chat.MutableComponent.create(new LiteralContents(message)));
        // ELSE IF >= forge-1.18
//        context.getSource().sendSuccess(new net.minecraft.network.chat.TextComponent(message), false);
        // ELSE
//        CommandContext<net.minecraft.command.CommandSource> context = (CommandContext<net.minecraft.command.CommandSource>) object;
//        context.getSource().sendSuccess(new net.minecraft.util.text.StringTextComponent(message), false);
        // END IF
    }
}