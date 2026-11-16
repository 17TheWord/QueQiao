package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
// IF < fabric-1.20.4
//import net.minecraft.advancement.Advancement;
// ELSE
//import net.minecraft.advancement.AdvancementEntry;
// END IF

/**
 * 玩家成就事件回调：由 Mixin 触发，供 {@code EventProcessor} 统一处理。
 */
public interface PlayerAdvancementCallback {

    Event<PlayerAdvancementCallback> EVENT = EventFactory.createArrayBacked(
            PlayerAdvancementCallback.class,
            listeners -> (player, advancement) -> {
                for (PlayerAdvancementCallback listener : listeners) {
                    listener.onAdvancement(player, advancement);
                }
            }
    );

    // IF < fabric-1.20.4
//    void onAdvancement(ServerPlayerEntity player, Advancement advancement);
    // ELSE
//    void onAdvancement(ServerPlayerEntity player, AdvancementEntry advancement);
    // END IF
}
