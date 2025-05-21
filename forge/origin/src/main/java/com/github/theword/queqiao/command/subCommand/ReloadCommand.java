package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import net.minecraft.command.ICommandSender;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class ReloadCommand extends ForgeSubCommand {

    public ReloadCommand() {
        super(new InnerReloadCommand());
    }

    public static class InnerReloadCommand extends ReloadCommandAbstract {

    }
    @Override
    public int onCommand(ICommandSender sender) {
        if (!handleCommandReturnMessageService.hasPermission(sender, inner.getPermissionNode())) return 0;
        inner.execute(sender, true);
        return 1;
    }
}