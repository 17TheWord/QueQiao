package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApi;
import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonSendTitle;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
import com.github.theword.queqiao.utils.ParseJsonToEvent;
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
import org.java_websocket.WebSocket;

import java.util.List;
// IF <= fabric-1.18.2
//import java.util.UUID;
//
//import net.minecraft.network.MessageType;
// END IF

import static com.github.theword.queqiao.QueQiao.minecraftServer;

public class HandleApiService implements HandleApi {
    private final ParseJsonToEvent parseJsonToEvent = new ParseJsonToEvent();

    /**
     * 广播消息
     *
     * @param webSocket   websocket
     * @param messageList 消息体
     */
    @Override
    public void handleBroadcastMessage(WebSocket webSocket, List<CommonTextComponent> messageList) {
        MutableText mutableText = parseJsonToEvent.parsePerMessageToMultiText(Tool.getPrefixComponent());
        mutableText.append(parseJsonToEvent.parseMessages(messageList));
        // IF >= fabric-1.19.2
//        sendPacket(new GameMessageS2CPacket(mutableText, false));
        // ELSE IF  fabric-1.18.2
// sendPacket(new GameMessageS2CPacket(mutableText, MessageType.CHAT, UUID.randomUUID()));
        // END IF
    }

    /**
     * 广播 Send Title 消息
     *
     * @param webSocket       websocket
     * @param commonSendTitle Send Title 消息体
     */
    @Override
    public void handleSendTitleMessage(WebSocket webSocket, CommonSendTitle commonSendTitle) {
        // IF > fabric-1.16.5
//        sendPacket(new TitleS2CPacket(parseJsonToEvent.parseMessages(commonSendTitle.getTitle())));
//        if (commonSendTitle.getSubtitle() != null)
//            sendPacket(new SubtitleS2CPacket(parseJsonToEvent.parseMessages(commonSendTitle.getSubtitle())));
//        sendPacket(new TitleFadeS2CPacket(commonSendTitle.getFadein(), commonSendTitle.getStay(), commonSendTitle.getFadeout()));
        // ELSE
//        sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, parseJsonToEvent.parseMessages(commonSendTitle.getTitle()), commonSendTitle.getFadein(), commonSendTitle.getStay(), commonSendTitle.getFadeout()));
//        if (commonSendTitle.getSubtitle() != null)
//            sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, parseJsonToEvent.parseMessages(commonSendTitle.getSubtitle()), commonSendTitle.getFadein(), commonSendTitle.getStay(), commonSendTitle.getFadeout()));
        // END IF
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param webSocket   websocket
     * @param messageList Action Bar 消息体
     */
    @Override
    public void handleActionBarMessage(WebSocket webSocket, List<CommonBaseComponent> messageList) {
        // IF >= fabric-1.19
//        sendPacket(new GameMessageS2CPacket(parseJsonToEvent.parseMessages(messageList), true));
        // ELSE
//        sendPacket(new GameMessageS2CPacket(parseJsonToEvent.parseMessages(messageList), MessageType.GAME_INFO, UUID.randomUUID()));
        // END IF
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(packet);
        }
    }
}