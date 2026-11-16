package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 玩家死亡事件回调：由 Mixin 触发，供 {@code EventProcessor} 统一处理。
 */
public interface PlayerDeathCallback {

    Event<PlayerDeathCallback> EVENT = EventFactory.createArrayBacked(
            PlayerDeathCallback.class,
            listeners -> player -> {
                for (PlayerDeathCallback listener : listeners) {
                    listener.onDeath(player);
                }
            }
    );

    void onDeath(ServerPlayerEntity player);
}
