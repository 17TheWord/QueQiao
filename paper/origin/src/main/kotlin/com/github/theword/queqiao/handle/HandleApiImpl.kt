package com.github.theword.queqiao.handle

import com.github.theword.queqiao.QueQiao.Companion.instance
import com.github.theword.queqiao.tool.GlobalContext
import com.github.theword.queqiao.tool.handle.HandleApiService
import com.github.theword.queqiao.tool.response.PrivateMessageResponse
import com.github.theword.queqiao.utils.PaperTool
import com.google.gson.JsonElement
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import java.util.*


class HandleApiImpl : HandleApiService {
    override fun handleBroadcastMessage(jsonElement: JsonElement) {
        val prefix: Component = PaperTool.buildComponent(GlobalContext.getMessagePrefixJsonObject())
        val message: Component = PaperTool.buildComponent(jsonElement)
        val result: Component = prefix.append(message)
        instance.server.sendMessage(result)
    }

    override fun handleSendTitleMessage(
        titleJsonElement: JsonElement?,
        subtitleJsonElement: JsonElement?,
        fadein: Int,
        stay: Int,
        fadeout: Int
    ) {
        var title: Component? = Component.empty()
        var subtitle: Component? = Component.empty()
        if (titleJsonElement != null && !titleJsonElement.isJsonNull) {
            title = PaperTool.buildComponent(titleJsonElement)
        }
        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull) {
            subtitle = PaperTool.buildComponent(subtitleJsonElement)
        }
        val times: Title.Times = Title.Times.times(
            Ticks.duration(fadein.toLong()), Ticks.duration(stay.toLong()), Ticks.duration(
                fadeout.toLong()
            )
        )
        title?.let { subtitle?.let { it1 -> instance.server.showTitle(Title.title(it, it1, times)) } }
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param jsonElement 消息体
     */
    override fun handleSendPrivateMessage(
        nickname: String?,
        uuid: UUID?,
        jsonElement: JsonElement
    ): PrivateMessageResponse {
        val targetPlayer = if (uuid != null) instance.server.getPlayer(uuid)
        else if (nickname != null && !nickname.isEmpty()) instance.server.getPlayer(nickname)
        else {
            return PrivateMessageResponse.playerNotFound()
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull()
        }

        if (!targetPlayer.isOnline) {
            return PrivateMessageResponse.playerNotOnline()
        }

        val prefix: Component = PaperTool.buildComponent(GlobalContext.getMessagePrefixJsonObject())
        val message: Component = PaperTool.buildComponent(jsonElement)

        val textComponent: Component = prefix.append(message)
        targetPlayer.sendMessage(textComponent)
        return PrivateMessageResponse.sendSuccess(PaperTool.getPaperPlayer(targetPlayer))
    }

    override fun handleSendActionBarMessage(jsonElement: JsonElement) {
        val message: Component = PaperTool.buildComponent(jsonElement)
        instance.server.sendActionBar(message)
    }
}