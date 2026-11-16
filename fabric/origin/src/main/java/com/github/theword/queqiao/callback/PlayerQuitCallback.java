package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 玩家退出事件回调：由 Mixin 触发，供 {@code EventProcessor} 统一处理。
 */
public interface PlayerQuitCallback {

    Event<PlayerQuitCallback> EVENT = EventFactory.createArrayBacked(
            PlayerQuitCallback.class,
            listeners -> player -> {
                for (PlayerQuitCallback listener : listeners) {
                    listener.onQuit(player);
                }
            }
    );

    void onQuit(ServerPlayerEntity player);
}
