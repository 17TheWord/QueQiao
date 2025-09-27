package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public final class QueQiao extends JavaPlugin {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new EventProcessor(), this);

        PluginCommand command = getCommand(BaseConstant.COMMAND_HEADER);
        if (command != null) command.setExecutor(new CommandExecutor());
    }

    @Override
    public void onDisable() {
        GlobalContext.shutdown();
    }
}