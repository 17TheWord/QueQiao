package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import com.github.theword.queqiao.tool.utils.Tool;
// IF >= fabric-1.20
//import net.minecraft.network.packet.Packet;
// ELSE IF >= fabric-1.18
//import net.minecraft.network.Packet;
// ELSE
//import net.minecraft.network.Packet;
// END IF

// IF > fabric-1.16.5
//import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
//import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
// END IF
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.List;
import java.util.UUID;
// IF <= fabric-1.18.2
//import java.util.UUID;
//import net.minecraft.network.MessageType;
// END IF

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

public class HandleApiImpl implements HandleApiService {
    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    /**
     * 广播消息
     *
     * @param messageList 消息体
     */
    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        MutableText mutableText = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        mutableText.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        // IF >= fabric-1.19.2
//        sendPacket(new GameMessageS2CPacket(mutableText, false));
        // ELSE IF  fabric-1.18.2
// sendPacket(new GameMessageS2CPacket(mutableText, MessageType.CHAT, UUID.randomUUID()));
        // END IF
    }

    /**
     * 广播 Send Title 消息
     *
     * @param titlePayload Title 消息体
     */
    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        // IF > fabric-1.16.5
//        sendPacket(new TitleS2CPacket(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle())));
//        if (titlePayload.getSubtitle() != null)
//            sendPacket(new SubtitleS2CPacket(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle())));
//        sendPacket(new TitleFadeS2CPacket(titlePayload.getFadein(), titlePayload.getStay(), titlePayload.getFadeout()));
        // ELSE
//        sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle()), titlePayload.getFadein(), titlePayload.getStay(), titlePayload.getFadeout()));
//        if (titlePayload.getSubtitle() != null)
//            sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle()), titlePayload.getFadein(), titlePayload.getStay(), titlePayload.getFadeout()));
        // END IF
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param messageList Action Bar 消息体
     */
    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        // IF >= fabric-1.19
//        sendPacket(new GameMessageS2CPacket(parseJsonToEventImpl.parseMessageListToComponent(messageList), true));
        // ELSE
//        sendPacket(new GameMessageS2CPacket(parseJsonToEventImpl.parseMessageListToComponent(messageList), MessageType.GAME_INFO, UUID.randomUUID()));
        // END IF
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param messageList 消息体
     */
    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, List<MessageSegment> messageList) {
        ServerPlayerEntity serverPlayerEntity;
        if (uuid != null)
            serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(uuid);
        else if (nickname != null && !nickname.isEmpty())
            serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(nickname);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (serverPlayerEntity == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (serverPlayerEntity.isDisconnected()) {
            return PrivateMessageResponse.playerNotOnline();
        }
        // IF >= fabric-1.19
//        serverPlayerEntity.sendMessage(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        // ELSE
//        serverPlayerEntity.sendMessage(parseJsonToEventImpl.parseMessageListToComponent(messageList), false);
        // END IF
        return PrivateMessageResponse.sendSuccess(getFabricPlayer(serverPlayerEntity));
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(packet);
        }
    }
}