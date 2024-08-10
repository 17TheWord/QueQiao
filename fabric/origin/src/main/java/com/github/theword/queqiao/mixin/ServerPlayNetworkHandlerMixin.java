package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.constant.BaseConstant;
import com.github.theword.queqiao.tool.event.fabric.FabricServerCommandMessageEvent;
import com.github.theword.queqiao.tool.event.fabric.FabricServerMessageEvent;
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

import java.util.Objects;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;
import static com.github.theword.queqiao.tool.utils.Tool.config;
import static com.github.theword.queqiao.tool.utils.Tool.sendWebsocketMessage;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        // IF > fabric-1.18.2
//        String message = packet.chatMessage();
        // ELSE IF fabric-1.18.2
//        String message = packet.getChatMessage();
        // END IF
        if (message.startsWith("/")) return;
        if (!config.getSubscribe_event().isPlayer_chat()) return;

        FabricServerMessageEvent event = new FabricServerMessageEvent("", getFabricPlayer(player), message);
        sendWebsocketMessage(event);
    }

    // IF fabric-1.21 || fabric-1.20.1 || fabric-1.19.2
//    @Inject(method = "onCommandExecution", at = @At("HEAD"))
//    private void onCommandExecution(CommandExecutionC2SPacket packet, CallbackInfo ci) {
//        String input = packet.command();
    // ELSE IF fabric-1.18.2
//    @Inject(method = "executeCommand", at = @At("HEAD"))
//    private void executeCommand(String input, CallbackInfo ci) {
        // END IF
        if (!config.getSubscribe_event().isPlayer_command()) return;

        if (!(input.startsWith("/l ") || input.startsWith("/login ") || input.startsWith("/register ") || input.startsWith("/reg ") || input.startsWith("/" + BaseConstant.COMMAND_HEADER + " "))) {
            FabricServerCommandMessageEvent event = new FabricServerCommandMessageEvent("", getFabricPlayer(Objects.requireNonNull(player)), input);
            sendWebsocketMessage(event);
        }
    }
}