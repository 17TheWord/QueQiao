package com.github.theword.queqiao

import com.github.theword.queqiao.api.UnsupportedPrivateMessageApi
import com.github.theword.queqiao.config.Config
import com.github.theword.queqiao.utils.FileWatcher
import com.github.theword.queqiao.utils.LineProcessor
import io.github.theword.queqiao.core.api.Api
import io.github.theword.queqiao.core.api.SendPrivateMessageApi
import io.github.theword.queqiao.core.constant.BaseConstant
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import org.slf4j.LoggerFactory
import java.nio.file.Paths
import java.util.function.Consumer

object QueQiao {

    @JvmStatic
    fun main(args: Array<String>) {
        val logger = LoggerFactory.getLogger(BaseConstant.MODULE_NAME)
        val regexConfig = Config.load(logger)

        val platform = VanillaPlatformContext()
        val apiConfigurer = Consumer<List<Api<*, *>>> { apis ->
            val mutableApis = apis as MutableList<Api<*, *>>
            mutableApis.removeAll { it is SendPrivateMessageApi }
            mutableApis.add(UnsupportedPrivateMessageApi(logger))
        }

        val runtime = QueQiaoRuntime.create(null, platform, apiConfigurer)
        platform.bindRuntime(runtime)
        runtime.start()

        val eventProcessor = EventProcessor(runtime)
        val lineProcessor = LineProcessor(regexConfig, runtime, logger, eventProcessor)
        val logPath = Paths.get(regexConfig.logPath.replace('\\', '/'))
        val watcher = FileWatcher(logPath, regexConfig.charset, lineProcessor, logger)
        val watcherThread = Thread({ watcher.run() }, "QueQiao-LogWatcher").apply {
            isDaemon = false
            start()
        }

        java.lang.Runtime.getRuntime().addShutdownHook(Thread {
            watcher.stop()
            watcherThread.interrupt()
            runtime.shutdown()
        })

        try {
            watcherThread.join()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}
