package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.callback.PlayerChatCallback;
import com.github.theword.queqiao.callback.PlayerCommandCallback;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
// IF > fabric-1.18.2
//import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
// END IF
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    // IF > fabric-1.16.5
//    @Inject(method = "onChatMessage", at = @At("HEAD"))
    // ELSE
//    @Inject(method = "onGameMessage", at = @At("HEAD"))
    // END IF
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        // IF > fabric-1.18.2
//        String message = packet.chatMessage();
        // ELSE
//        String message = packet.getChatMessage();
        // END IF

        PlayerChatCallback.EVENT.invoker().onChat(player, message);
    }

    // IF > fabric-1.18.2
//    @Inject(method = "onCommandExecution", at = @At("HEAD"))
//    private void onCommandExecution(CommandExecutionC2SPacket packet, CallbackInfo ci) {
//        PlayerCommandCallback.EVENT.invoker().onCommand(player, packet.command());
//    }
    // ELSE IF fabric-1.18.2
//    @Inject(method = "executeCommand", at = @At("HEAD"))
//    private void executeCommand(String input, CallbackInfo ci) {
//        PlayerCommandCallback.EVENT.invoker().onCommand(player, input);
//    }
    // ELSE
//    @Inject(method = "executeCommand", at = @At("HEAD"))
//    private void executeCommand(String input, CallbackInfo ci) {
//        PlayerCommandCallback.EVENT.invoker().onCommand(player, input);
//    }
    // END IF
}
