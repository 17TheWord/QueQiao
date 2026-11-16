package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 玩家加入事件回调：由 Mixin 触发，供 {@code EventProcessor} 统一处理。
 */
public interface PlayerJoinCallback {

    Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(
            PlayerJoinCallback.class,
            listeners -> player -> {
                for (PlayerJoinCallback listener : listeners) {
                    listener.onJoin(player);
                }
            }
    );

    void onJoin(ServerPlayerEntity player);
}
