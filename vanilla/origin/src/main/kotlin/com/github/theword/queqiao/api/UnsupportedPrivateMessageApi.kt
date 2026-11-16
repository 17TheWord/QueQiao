package com.github.theword.queqiao.api

import io.github.theword.queqiao.core.api.Api
import io.github.theword.queqiao.core.constant.ProtocolConstants
import io.github.theword.queqiao.core.payload.PrivateMessagePayload
import io.github.theword.queqiao.core.response.PrivateMessageResponse
import org.slf4j.Logger

/**
 * 独立端没有玩家体系，私聊固定返回旧版语义的“不可用”。
 */
class UnsupportedPrivateMessageApi(logger: Logger) :
    Api<PrivateMessagePayload, PrivateMessageResponse>(PrivateMessagePayload::class.java, logger) {

    override fun name(): String {
        return ProtocolConstants.Api.SEND_PRIVATE_MSG
    }

    public override fun doExecute(payload: PrivateMessagePayload): PrivateMessageResponse {
        return PrivateMessageResponse.of(null, "该接口不可用")
    }
}
