package com.github.theword.queqiao.handle

import com.github.theword.queqiao.tool.handle.HandleApiService
import com.github.theword.queqiao.tool.response.PrivateMessageResponse
import com.google.gson.JsonElement
import java.util.UUID


class HandleApiImpl : HandleApiService {
    override fun handleBroadcastMessage(jsonElement: JsonElement) {
    }

    override fun handleSendTitleMessage(
        titleElement: JsonElement,
        subtitleElement: JsonElement,
        fadein: Int,
        stay: Int,
        fadeout: Int,
    ) {
    }

    override fun handleSendActionBarMessage(jsonElement: JsonElement) {
    }

    override fun handleSendPrivateMessage(s: String, uuid: UUID, jsonElement: JsonElement): PrivateMessageResponse {
        return PrivateMessageResponse.of(null, "该接口不可用")
    }
}