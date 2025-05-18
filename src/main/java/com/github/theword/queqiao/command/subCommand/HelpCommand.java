package com.github.theword.queqiao.command.subCommand;

import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.HelpCommandAbstract;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class HelpCommand extends HelpCommandAbstract implements ForgeSubCommand {
    CommandTreeBase commandTree;
    public HelpCommand(CommandTreeBase tree){
        this.commandTree=tree;
    }
    @Override
    public int onCommand(ICommandSender sender) {
        if (!handleCommandReturnMessageService.hasPermission(sender, getPermissionNode())) return 0;
        handleCommandReturnMessageService.handleCommandReturnMessage(sender, "-------------------");
        for (ICommand subCommand : commandTree.getSubCommands()) {
            if(!(subCommand instanceof ForgeSubCommand)) continue;
            ForgeSubCommand forgeSubCommand = (ForgeSubCommand) subCommand;
            handleCommandReturnMessageService.handleCommandReturnMessage(sender, forgeSubCommand.getUsage() + "---" + forgeSubCommand.getDescription());
        }
        handleCommandReturnMessageService.handleCommandReturnMessage(sender, "-------------------");
        return 1;

    }
}