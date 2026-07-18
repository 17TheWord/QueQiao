package com.github.theword.queqiao.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerCommandCallback {

    Event<PlayerCommandCallback> EVENT = EventFactory.createArrayBacked(
            PlayerCommandCallback.class,
            listeners -> (player, command) -> {
                for (PlayerCommandCallback listener : listeners) {
                    listener.onCommand(player, command);
                }
            }
    );

    void onCommand(ServerPlayer player, String command);
}
