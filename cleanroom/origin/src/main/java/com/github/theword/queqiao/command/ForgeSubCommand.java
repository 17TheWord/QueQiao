package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.command.SubCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;


public abstract class ForgeSubCommand extends CommandBase {
    public final SubCommand inner;
    public ForgeSubCommand(SubCommand inner){
        this.inner=inner;
    }

    @Override
    public String getName(){
        return inner.getName();
    }

    @Override
    public String getUsage(ICommandSender sender){
        return inner.getUsage();
    }

    public abstract int onCommand(ICommandSender sender);

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        int result = onCommand(sender);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender){
        return sender.canUseCommand(BaseConstant.MOD_PERMISSION_LEVEL,"");
    };

    @Override
    public int compareTo(ICommand o) {
        return this.getName().compareTo(o.getName());
    }
}