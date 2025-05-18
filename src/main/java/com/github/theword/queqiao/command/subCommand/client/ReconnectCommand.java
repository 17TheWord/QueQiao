package com.github.theword.queqiao.command.subCommand.client;

import com.github.theword.queqiao.command.ForgeSubCommand;
import com.github.theword.queqiao.tool.command.subCommand.client.ReconnectCommandAbstract;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.theword.queqiao.tool.utils.Tool.handleCommandReturnMessageService;


public class ReconnectCommand extends ReconnectCommandAbstract implements ForgeSubCommand {

    @Override
    public int onCommand(ICommandSender sender) {
        return 0;
    }
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length==1) return Collections.singletonList("all");
        return Collections.emptyList();
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!handleCommandReturnMessageService.hasPermission(sender, getPermissionNode())) return;
        execute(sender, args.length>0 && Objects.equals(args[0], "all"));
    }
}