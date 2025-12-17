package com.github.theword.queqiao.command;

import com.github.theword.queqiao.command.subCommand.HelpCommand;
import com.github.theword.queqiao.command.subCommand.ReloadCommand;
import com.github.theword.queqiao.command.subCommand.client.ReconnectCommand;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTreeQueQiao extends CommandTreeBase {

    public CommandTreeQueQiao(){
        super();
        this.addSubcommand(new HelpCommand(this));
        this.addSubcommand(new ReloadCommand());
        this.addSubcommand(new ReconnectCommand());
    }

    @Override
    public String getName() {
        return BaseConstant.COMMAND_HEADER;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

}