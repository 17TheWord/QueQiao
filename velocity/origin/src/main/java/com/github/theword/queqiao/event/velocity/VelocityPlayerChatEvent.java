package com.github.theword.queqiao.event.velocity;

import com.github.theword.queqiao.tool.event.base.BasePlayerChatEvent;

public class VelocityPlayerChatEvent extends BasePlayerChatEvent {
    public VelocityPlayerChatEvent(VelocityPlayer player, String message){
        super("VelocityPlayerChatEvent", "", player, message);
    }
}
