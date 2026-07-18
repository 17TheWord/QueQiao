package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerAdvancementCallback {

    Event<PlayerAdvancementCallback> EVENT = EventFactory.createArrayBacked(
            PlayerAdvancementCallback.class,
            listeners -> (player, advancement) -> {
                for (PlayerAdvancementCallback listener : listeners) {
                    listener.onAdvancement(player, advancement);
                }
            }
    );

    void onAdvancement(ServerPlayer player, AdvancementHolder advancement);
}
