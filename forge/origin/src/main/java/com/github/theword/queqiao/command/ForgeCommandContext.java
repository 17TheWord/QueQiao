package com.github.theword.queqiao.command;

import io.github.theword.queqiao.core.command.CommandExecutionContext;
import io.github.theword.queqiao.core.constant.CommandConstant;

// IF > forge-1.16.5
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.network.chat.Component;
// ELSE
//import net.minecraft.command.CommandSource;
//import net.minecraft.util.text.StringTextComponent;
// END IF

// IF forge-1.18.2
//import net.minecraft.network.chat.TextComponent;
// END IF

/**
 * 一次命令调用的执行上下文（Forge 版）。
 *
 * <p>命令源类型在不同 MC 版本下不同，因此对外使用 {@link Object}，内部按版本分支转型。
 */
public final class ForgeCommandContext extends CommandExecutionContext<Object> {

    public ForgeCommandContext(Object nativeSource) {
        super(nativeSource);
    }

    @Override
    public void reply(String message) {
        // IF >= forge-1.19
//        ((CommandSourceStack) nativeSource).sendSystemMessage(Component.literal(message));
        // ELSE IF >= forge-1.18
//        ((CommandSourceStack) nativeSource).sendSuccess(new TextComponent(message), false);
        // ELSE
//        ((CommandSource) nativeSource).sendSuccess(new StringTextComponent(message), false);
        // END IF
    }

    @Override
    public boolean hasPermission(String permission) {
        // MOD 端无权限节点，统一按权限等级判定
        // IF > forge-1.16.5
//        return ((CommandSourceStack) nativeSource).hasPermission(CommandConstant.MOD_PERMISSION_LEVEL);
        // ELSE
//        return ((CommandSource) nativeSource).hasPermission(CommandConstant.MOD_PERMISSION_LEVEL);
        // END IF
    }
}
