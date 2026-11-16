package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 玩家命令事件回调：由 Mixin 触发，供 {@code EventProcessor} 统一处理。
 */
public interface PlayerCommandCallback {

    Event<PlayerCommandCallback> EVENT = EventFactory.createArrayBacked(
            PlayerCommandCallback.class,
            listeners -> (player, command) -> {
                for (PlayerCommandCallback listener : listeners) {
                    listener.onCommand(player, command);
                }
            }
    );

    void onCommand(ServerPlayerEntity player, String command);
}
