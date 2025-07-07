package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.constant.ServerTypeConstant;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.theword.queqiao.tool.utils.Tool.initTool;
import static com.github.theword.queqiao.tool.utils.Tool.websocketManager;

public final class QueQiao extends JavaPlugin {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        initTool(
                false,
                instance.getServer().getVersion(),
                ServerTypeConstant.SPIGOT,
                new HandleApiImpl(),
                new HandleCommandReturnMessageImpl()
        );
        websocketManager.startWebsocketOnServerStart();
        Bukkit.getPluginManager().registerEvents(new EventProcessor(), this);

        PluginCommand command = getCommand(BaseConstant.COMMAND_HEADER);
        if (command != null) command.setExecutor(new CommandExecutor());
    }

    @Override
    public void onDisable() {
        websocketManager.stopWebsocketByServerClose();
    }
}