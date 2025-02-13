package com.github.theword.queqiao

import com.github.theword.queqiao.config.Config
import com.github.theword.queqiao.handle.HandleApiImpl
import com.github.theword.queqiao.handle.HandleCommandReturnMessageImpl
import com.github.theword.queqiao.tool.constant.BaseConstant
import com.github.theword.queqiao.tool.constant.ServerTypeConstant
import com.github.theword.queqiao.tool.utils.Tool.*
import com.github.theword.queqiao.utils.FileWatcher
import com.github.theword.queqiao.utils.LineProcessor

object QueQiao {
    @JvmStatic
    fun main(args: Array<String>) {
        initTool(
            true,
            BaseConstant.UNKNOWN,
            ServerTypeConstant.ORIGIN,
            HandleApiImpl(),
            HandleCommandReturnMessageImpl()
        )
        websocketManager.startWebsocket(null)
        val config = Config()
        FileWatcher.fileListen(config.logPath, LineProcessor(config, EventProcessor()))
    }
}