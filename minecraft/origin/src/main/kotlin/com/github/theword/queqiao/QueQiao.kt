package com.github.theword.queqiao

import com.github.theword.queqiao.config.Config
import com.github.theword.queqiao.handle.HandleApiImpl
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl
import com.github.theword.queqiao.tool.utils.Tool.initTool
import com.github.theword.queqiao.tool.utils.Tool.websocketManager
import com.github.theword.queqiao.utils.FileWatcher
import com.github.theword.queqiao.utils.LineProcessor

object QueQiao {
    @JvmStatic
    fun main(args: Array<String>) {
        initTool(true, HandleApiImpl(), HandleCommandReturnMessageImpl())
        websocketManager.startWebsocket(null)
        val config = Config()
        FileWatcher.fileListen(config.logPath, LineProcessor(config, EventProcessor()))
    }
}
