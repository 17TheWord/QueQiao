package com.github.theword.queqiao;

import com.github.theword.queqiao.command.CommandExecutor;
import com.github.theword.queqiao.handle.HandleApiService;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageService;
import com.github.theword.queqiao.tool.constant.BaseConstant;
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
        initTool(false, new HandleApiService(), new HandleCommandReturnMessageService());
        websocketManager.startWebsocket(null);
        Bukkit.getPluginManager().registerEvents(new EventProcessor(), this);

        PluginCommand command = getCommand(BaseConstant.COMMAND_HEADER);
        if (command != null)
            command.setExecutor(new CommandExecutor());
    }

    @Override
    public void onDisable() {
        websocketManager.stopWebsocketByServerClose();
    }
}
