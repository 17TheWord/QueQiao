package com.github.theword.queqiao.handle

import com.github.theword.queqiao.tool.handle.HandleApiService
import com.github.theword.queqiao.tool.payload.MessageSegment
import com.github.theword.queqiao.tool.payload.TitlePayload
import com.github.theword.queqiao.tool.response.PrivateMessageResponse
import java.util.UUID


class HandleApiImpl : HandleApiService {
    override fun handleBroadcastMessage(list: List<MessageSegment>) {
    }

    override fun handleSendTitleMessage(titlePayload: TitlePayload) {
    }

    override fun handleSendActionBarMessage(list: List<MessageSegment>) {
    }

    override fun handleSendPrivateMessage(s: String, uuid: UUID, list: List<MessageSegment>): PrivateMessageResponse {
        return PrivateMessageResponse.of(null, "该接口不可用")
    }
}
