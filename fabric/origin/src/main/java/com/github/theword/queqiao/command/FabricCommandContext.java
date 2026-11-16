package com.github.theword.queqiao.command;

import com.github.theword.queqiao.utils.FabricTool;
import io.github.theword.queqiao.core.command.CommandExecutionContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * 一次命令调用的执行上下文（Fabric 版）。
 */
public final class FabricCommandContext extends CommandExecutionContext<ServerCommandSource> {

    public FabricCommandContext(ServerCommandSource nativeSource) {
        super(nativeSource);
    }

    @Override
    public void reply(String message) {
        // IF >= fabric-1.20
//        nativeSource.sendFeedback(() -> Text.of(message), false);
        // ELSE
//        nativeSource.sendFeedback(Text.of(message), false);
        // END IF
    }

    @Override
    public boolean hasPermission(String permission) {
        // MOD 端无权限节点，统一按权限等级判定
        return FabricTool.permissionCheck(nativeSource);
    }
}
