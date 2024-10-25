package com.github.theword.queqiao;

import com.github.theword.queqiao.config.Config;
import com.github.theword.queqiao.handle.HandleApiImpl;
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl;
import com.github.theword.queqiao.utils.FileWatcher;
import com.github.theword.queqiao.utils.LineProcessor;


import static com.github.theword.queqiao.tool.utils.Tool.*;

public class QueQiao {
    public static void main(String[] args) {
        initTool(true, new HandleApiImpl(), new HandleCommandReturnMessageImpl());
        websocketManager.startWebsocket(null);
        Config config = new Config();
        FileWatcher.fileListen(config.getLogPath(), new LineProcessor(config, new EventProcessor()));
    }
}
