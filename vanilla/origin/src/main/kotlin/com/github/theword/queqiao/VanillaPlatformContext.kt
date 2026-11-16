package com.github.theword.queqiao

import com.github.theword.queqiao.utils.JsonComponentText
import com.github.theword.queqiao.utils.RconMessageSender
import com.google.gson.JsonElement
import io.github.theword.queqiao.core.constant.BaseConstant
import io.github.theword.queqiao.core.constant.ServerTypeConstant
import io.github.theword.queqiao.core.event.model.PlayerModel
import io.github.theword.queqiao.core.platform.AbstractPlatformContext
import io.github.theword.queqiao.core.platform.PlatformResult
import io.github.theword.queqiao.core.platform.PlatformResultCode
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

/**
 * 独立端平台上下文。
 *
 * <p>独立端没有服务端对象和在线玩家，平台能力只保留：
 * 元数据、数据目录、RCON 消息发送。
 */
class VanillaPlatformContext : AbstractPlatformContext<Any?, String, String>(null) {

    private val logger = LoggerFactory.getLogger(BaseConstant.MODULE_NAME)

    @Volatile
    private var runtime: QueQiaoRuntime? = null

    @Volatile
    private var sender: RconMessageSender? = null

    fun bindRuntime(runtime: QueQiaoRuntime) {
        this.runtime = runtime
        this.sender = RconMessageSender(runtime, { JsonComponentText.flatten(runtime.getMessagePrefixJsonElement()) }, logger)
    }

    override fun getServerType(): String {
        return ServerTypeConstant.ORIGIN
    }

    override fun getServerVersion(): String {
        return BaseConstant.UNKNOWN
    }

    /**
     * 独立端沿用旧版的 config/QueQiao 目录，不能使用 core 对非模组端默认的 plugins/QueQiao。
     */
    override fun getDataDirectory(): Path {
        return Paths.get("config", BaseConstant.MODULE_NAME)
    }

    override fun jsonToComponent(json: JsonElement): String {
        return JsonComponentText.flatten(json)
    }

    override fun getPlayers(): Collection<String> {
        return emptyList()
    }

    override fun getPlayerName(player: String): String {
        return player
    }

    override fun getPlayerUUID(player: String): UUID {
        // 独立端没有在线玩家，core 不会用这个值做匹配
        return UUID(0, 0)
    }

    override fun getPlayerModel(player: String): PlayerModel {
        return PlayerModel(player)
    }

    override fun broadcast(component: String?): PlatformResult<Void> {
        return sender().broadcast(component)
    }

    override fun sendPrivateMessage(player: String, component: String?): PlatformResult<Void> {
        return PlatformResult.failure(PlatformResultCode.UNSUPPORTED, "该接口不可用")
    }

    override fun sendTitleComponent(
        title: String?,
        subtitle: String?,
        fadeIn: Int,
        stay: Int,
        fadeOut: Int
    ): PlatformResult<Void> {
        return sender().sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

    override fun sendActionBarComponent(component: String?): PlatformResult<Void> {
        return sender().sendActionBar(component)
    }

    private fun sender(): RconMessageSender {
        return sender ?: RconMessageSender(runtime, { runtime?.getMessagePrefixJsonElement()?.let { JsonComponentText.flatten(it) } }, logger)
    }
}
