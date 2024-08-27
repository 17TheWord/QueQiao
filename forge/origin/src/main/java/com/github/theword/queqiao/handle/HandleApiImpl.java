package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
// IF > forge-1.16.5
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
//import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
//import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
//import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
//import net.minecraft.server.level.ServerPlayer;
// ELSE
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.network.IPacket;
//import net.minecraft.network.play.server.SChatPacket;
//import net.minecraft.network.play.server.STitlePacket;
//import net.minecraft.util.text.ChatType;
//import net.minecraft.util.text.StringTextComponent;
// END IF

// IF < forge-1.19
//import java.util.UUID;
// END IF

import org.java_websocket.WebSocket;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;


public class HandleApiImpl implements HandleApiService {
    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    /**
     * 广播消息
     *
     * @param webSocket   websocket
     * @param messageList 消息体
     */
    @Override
    public void handleBroadcastMessage(WebSocket webSocket, List<MessageSegment> messageList) {
        // IF > forge-1.16.5
//        MutableComponent mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        // ELSE
//        StringTextComponent mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        // END IF

        mutableComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));

        // IF < forge-1.19
//        UUID uuid = UUID.randomUUID();
        // END IF

        // IF > forge-1.16.5
//        for (ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
        // ELSE
//        for (ServerPlayerEntity serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            // END IF

            // IF >= forge-1.19
//            serverPlayer.sendSystemMessage(mutableComponent);
            // ELSE
//            serverPlayer.sendMessage(mutableComponent, uuid);
            // END IF
        }
    }

    /**
     * 广播 Send Title 消息
     *
     * @param webSocket    websocket
     * @param titlePayload Send Title 消息体
     */
    @Override
    public void handleSendTitleMessage(WebSocket webSocket, TitlePayload titlePayload) {
        // IF > forge-1.16.5
//        sendPacket(new ClientboundSetTitleTextPacket(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle())));
//        if (titlePayload.getSubtitle() != null)
//            sendPacket(new ClientboundSetSubtitleTextPacket(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle())));
//        sendPacket(new ClientboundSetTitlesAnimationPacket(titlePayload.getFadein(), titlePayload.getStay(), titlePayload.getFadeout()));
        // ELSE
//        sendPacket(new STitlePacket(STitlePacket.Type.TITLE, parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle())));
//        if (titlePayload.getSubtitle() != null)
//            sendPacket(new STitlePacket(STitlePacket.Type.SUBTITLE, parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle())));
//        sendPacket(new STitlePacket(titlePayload.getFadein(), titlePayload.getStay(), titlePayload.getFadeout()));
        // END IF
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param webSocket   websocket
     * @param messageList Action Bar 消息体
     */
    @Override
    public void handleActionBarMessage(WebSocket webSocket, List<MessageSegment> messageList) {
        // IF > forge-1.16.5
//        sendPacket(new ClientboundSetActionBarTextPacket(parseJsonToEventImpl.parseMessageListToComponent(messageList)));
        // ELSE
//        sendPacket(new SChatPacket(parseJsonToEventImpl.parseMessageListToComponent(messageList), ChatType.GAME_INFO, UUID.randomUUID()));
        // END IF
    }

    /**
     * 私聊消息
     *
     * @param webSocket        websocket
     * @param targetPlayerName 目标玩家名称
     * @param targetPlayerUuid 目标玩家 UUID
     * @param messageList      消息体
     */
    @Override
    public void handlePrivateMessage(WebSocket webSocket, String targetPlayerName, UUID targetPlayerUuid, List<MessageSegment> messageList) {
        // IF > forge-1.16.5
//        ServerPlayer targetPlayer;
        // ELSE
//        ServerPlayerEntity targetPlayer;
        // END IF
        if (targetPlayerUuid != null)
            targetPlayer = minecraftServer.getPlayerList().getPlayer(targetPlayerUuid);
        else if (targetPlayerName != null && !targetPlayerName.isEmpty())
            targetPlayer = minecraftServer.getPlayerList().getPlayerByName(targetPlayerName);
        else {
//            webSocket.send("{\"code\":400,\"message\":\"Target player not found.\"}");
            return;
        }

        if (targetPlayer == null) {
//            webSocket.send("{\"code\":400,\"message\":\"Target player is null.\"}");
            return;
        }

        if (targetPlayer.hasDisconnected()) {
//            webSocket.send("{\"code\":400,\"message\":\"Target player is disconnected.\"}");
            return;
        }
        // IF > forge-1.16.5
//        MutableComponent mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
//        mutableComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        // ELSE
//        StringTextComponent mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
//        mutableComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        // END IF

        // IF >= forge-1.19
//            targetPlayer.sendSystemMessage(mutableComponent);
        // ELSE
//        targetPlayer.sendMessage(mutableComponent, UUID.randomUUID());
        // END IF

//        webSocket.send("{\"code\":200,\"message\":\"Private message sent.\"}");
    }

    // IF > forge-1.16.5
//    private void sendPacket(Packet<?> packet) {
//        for (ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
//            serverPlayer.connection.send(packet);
//        }
//    }
    // ELSE
//    private void sendPacket(IPacket<?> packet) {
//        for (ServerPlayerEntity serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
//            serverPlayer.connection.send(packet);
//        }
//    }
    // END IF
}