package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;


public class HelpCommand extends ForgeSubCommand {
    CommandTreeBase commandTree;

    public HelpCommand(CommandTreeBase tree){
        super(new InnerHelpCommand());
        this.commandTree=tree;
    }

    public static class InnerHelpCommand extends HelpCommandAbstract {

    }

    @Override
    public int onCommand(ICommandSender sender) {
        if (!GlobalContext.getHandleCommandReturnMessageService().hasPermission(sender, inner.getPermissionNode())) return 0;
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(sender, "-------------------");
        for (ICommand subCommand : commandTree.getSubCommands()) {
            if(!(subCommand instanceof ForgeSubCommand)) continue;
            ForgeSubCommand forgeSubCommand = (ForgeSubCommand) subCommand;
            GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(sender, forgeSubCommand.getUsage(sender) + "---" + forgeSubCommand.inner.getDescription());
        }
        GlobalContext.getHandleCommandReturnMessageService().handleCommandReturnMessage(sender, "-------------------");
        return 1;
    }
}