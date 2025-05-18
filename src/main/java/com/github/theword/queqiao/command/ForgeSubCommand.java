package com.github.theword.queqiao.command;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public interface ForgeSubCommand extends ICommand {

    String getName();

    String getDescription();

    String getUsage();

    default List<String> getAliases(){
        return Collections.emptyList();
    }

    default String getUsage(ICommandSender sender){
        return getUsage();
    }

    int onCommand(ICommandSender sender);

    @Override
    default void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        int result = onCommand(sender);
    }

    @Override
    default boolean checkPermission(MinecraftServer server, ICommandSender sender){
        return sender.canUseCommand(BaseConstant.MOD_PERMISSION_LEVEL,"");
    };

    @Override
    default List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }

    @Override
    default boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    default int compareTo(ICommand o) {
        return this.getName().compareTo(o.getName());
    }
}