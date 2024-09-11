package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.handle.HandleCommandReturnMessageService;
import com.mojang.brigadier.context.CommandContext;
// IF >= forge-1.21
//import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
// ELSE IF >= forge-1.19
//import net.minecraft.network.chat.contents.LiteralContents;
// END IF
// IF >= forge-1.18
// END IF

public class HandleCommandReturnMessageImpl implements HandleCommandReturnMessageService {

    @Override
    @SuppressWarnings("unchecked")
    public void handleCommandReturnMessage(Object object, String message) {
        // IF > forge-1.16.5
//        CommandContext<net.minecraft.commands.CommandSourceStack> context = (CommandContext<net.minecraft.commands.CommandSourceStack>) object;
        // END IF
        // IF >= forge-1.19
//        context.getSource().sendSystemMessage(net.minecraft.network.chat.MutableComponent.create(new LiteralContents(message)));
        // ELSE IF >= forge-1.18
//            context.getSource().sendSuccess(new net.minecraft.network.chat.TextComponent(message), false);
        // ELSE
//        CommandContext<net.minecraft.command.CommandSource> context = (CommandContext<net.minecraft.command.CommandSource>) object;
//            context.getSource().sendSuccess(new net.minecraft.util.text.StringTextComponent(message), false);
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
        // IF > forge-1.16.5
//        CommandContext<net.minecraft.commands.CommandSourceStack> context = (CommandContext<net.minecraft.commands.CommandSourceStack>) object;
        // ELSE
//        CommandContext<net.minecraft.command.CommandSource> context = (CommandContext<net.minecraft.command.CommandSource>) object;
        // END IF
        if (context.getSource().hasPermission(BaseConstant.MOD_PERMISSION_LEVEL)) return true;
        handleCommandReturnMessage(object, "您没有权限执行此命令");
        return false;
    }
}