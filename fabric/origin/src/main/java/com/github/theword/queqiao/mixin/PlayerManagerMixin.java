package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.event.fabric.FabricServerPlayConnectionJoinEvent;

import com.github.theword.queqiao.tool.GlobalContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;

// IF >= fabric-1.20.4
//import net.minecraft.server.network.ConnectedClientData;
// END IF
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    // IF >= fabric-1.20.4
//    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        // ELSE
//    private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
// END IF
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;

        FabricServerPlayConnectionJoinEvent event = new FabricServerPlayConnectionJoinEvent(getFabricPlayer(player));
        GlobalContext.sendEvent(event);
    }
}