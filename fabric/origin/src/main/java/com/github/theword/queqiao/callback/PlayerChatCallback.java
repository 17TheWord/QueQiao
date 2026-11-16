package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 玩家聊天事件回调：由 Mixin 触发，供 {@code EventProcessor} 统一处理。
 */
public interface PlayerChatCallback {

    Event<PlayerChatCallback> EVENT = EventFactory.createArrayBacked(
            PlayerChatCallback.class,
            listeners -> (player, message) -> {
                for (PlayerChatCallback listener : listeners) {
                    listener.onChat(player, message);
                }
            }
    );

    void onChat(ServerPlayerEntity player, String message);
}
