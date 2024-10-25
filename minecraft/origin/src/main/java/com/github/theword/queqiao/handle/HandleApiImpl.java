package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;

import java.util.List;
import java.util.UUID;

public class HandleApiImpl implements HandleApiService {
    @Override
    public void handleBroadcastMessage(List<MessageSegment> list) {
    }

    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
    }

    @Override
    public void handleSendActionBarMessage(List<MessageSegment> list) {
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String s, UUID uuid, List<MessageSegment> list) {
        return PrivateMessageResponse.of(null, "该接口不可用");
    }
}
