package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.PaperSubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.ReloadCommandAbstract;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


public class ReloadCommand extends ReloadCommandAbstract implements PaperSubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(commandSender, getPermissionNode())) return false;
        execute(commandSender, false);
        return true;
    }

    @Override
    public List<String> getSubCommands(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPrefix() {
        return null;
    }
}