package com.github.theword.queqiao

import com.github.theword.queqiao.utils.PaperTool.Companion.buildComponent
import com.github.theword.queqiao.utils.PaperTool.Companion.getComponentText
import com.github.theword.queqiao.utils.PaperTool.Companion.getPaperPlayer
import com.google.gson.JsonElement
import io.github.theword.queqiao.core.constant.ServerTypeConstant
import io.github.theword.queqiao.core.event.model.PlayerModel
import io.github.theword.queqiao.core.event.model.TranslateModel
import io.github.theword.queqiao.core.platform.AbstractPlatformContext
import io.github.theword.queqiao.core.platform.PlatformResult
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import org.bukkit.Server
import org.bukkit.entity.Player
import java.util.UUID

/**
 * Paper 平台上下文：core 与 Paper 服务端之间的唯一接入点。
 *
 * 线程契约：core 可能从 WebSocket 工作线程直接调用这里的方法，
 * 而 Bukkit 的部分 API 要求主线程；本实现只使用线程安全的发送接口。
 */
class PlatformContext(server: Server) :
    AbstractPlatformContext<Server, Component, Player>(server) {

    /**
     * 由 [QueQiao] 在 Runtime 创建后回填，用于读取消息前缀。
     */
    var runtime: QueQiaoRuntime? = null
        private set

    fun bindRuntime(runtime: QueQiaoRuntime) {
        this.runtime = runtime
    }

    override fun getServerType(): String = ServerTypeConstant.PAPER

    override fun getServerVersion(): String = server.minecraftVersion

    override fun jsonToComponent(json: JsonElement): Component = buildComponent(json)

    override fun getPlayers(): Collection<Player> = server.onlinePlayers.toList()

    override fun getPlayerName(player: Player): String = player.name

    override fun getPlayerUUID(player: Player): UUID = player.uniqueId

    override fun getPlayerModel(player: Player): PlayerModel = getPaperPlayer(player)

    override fun broadcast(component: Component): PlatformResult<Void> {
        server.sendMessage(withPrefix(component))
        return PlatformResult.success<Void>(null)
    }

    override fun sendPrivateMessage(player: Player, component: Component): PlatformResult<Void> {
        player.sendMessage(withPrefix(component))
        return PlatformResult.success<Void>(null)
    }

    override fun sendTitleComponent(
        title: Component?,
        subtitle: Component?,
        fadeIn: Int,
        stay: Int,
        fadeOut: Int
    ): PlatformResult<Void> {
        val times = Title.Times.times(
            Ticks.duration(fadeIn.toLong()),
            Ticks.duration(stay.toLong()),
            Ticks.duration(fadeOut.toLong())
        )
        server.showTitle(Title.title(title ?: Component.empty(), subtitle ?: Component.empty(), times))
        return PlatformResult.success<Void>(null)
    }

    override fun sendActionBarComponent(component: Component): PlatformResult<Void> {
        server.sendActionBar(component)
        return PlatformResult.success<Void>(null)
    }

    /**
     * 把 Adventure 的翻译组件拆成 [TranslateModel]，翻译文本由平台翻译服务提供。
     */
    override fun parseTranslateModel(component: Component): TranslateModel {
        if (component !is TranslatableComponent) {
            return TranslateModel(null, null, getComponentText(component))
        }

        val key = component.key()
        val args = component.arguments()

        val childModels = arrayOfNulls<TranslateModel>(args.size)
        val stringsForFormat = arrayOfNulls<Any>(args.size)

        for (i in args.indices) {
            val child = parseTranslateModel(args[i].asComponent())
            childModels[i] = child
            stringsForFormat[i] = child.text
        }

        var finalText = translationService.translate(key, stringsForFormat)

        if (finalText == key) {
            finalText = getComponentText(component)
        } else if (component.children().isNotEmpty()) {
            val builder = StringBuilder(finalText)
            for (child in component.children()) {
                builder.append(parseTranslateModel(child).text)
            }
            finalText = builder.toString()
        }

        return TranslateModel(key, childModels, finalText)
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private fun withPrefix(component: Component): Component {
        val prefix: JsonElement = runtime?.messagePrefixJsonElement ?: return component
        return buildComponent(prefix).append(component)
    }
}
