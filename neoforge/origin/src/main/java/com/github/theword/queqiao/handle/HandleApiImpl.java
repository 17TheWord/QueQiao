package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.Queqiao.minecraftServer;
import static com.github.theword.queqiao.utils.NeoForgeTool.getNeoForgePlayer;


public class HandleApiImpl implements HandleApiService {
    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    /**
     * 广播消息
     *
     * @param messageList message
     */
    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        MutableComponent mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());

        mutableComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        sendPacket(new ClientboundSystemChatPacket(mutableComponent, false));
    }

    /**
     * 广播 Send Title 消息
     *
     * @param titlePayload Send Title message
     */
    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        sendPacket(new ClientboundSetTitleTextPacket(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle())));
        if (titlePayload.getSubtitle() != null)
            sendPacket(new ClientboundSetSubtitleTextPacket(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle())));
        sendPacket(new ClientboundSetTitlesAnimationPacket(titlePayload.getFadein(), titlePayload.getStay(), titlePayload.getFadeout()));
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param messageList Action Bar message
     */
    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        sendPacket(new ClientboundSetActionBarTextPacket(parseJsonToEventImpl.parseMessageListToComponent(messageList)));
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param messageList message
     */
    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, List<MessageSegment> messageList) {
        ServerPlayer targetPlayer;
        if (uuid != null)
            targetPlayer = minecraftServer.getPlayerList().getPlayer(uuid);
        else if (nickname != null && !nickname.isEmpty())
            targetPlayer = minecraftServer.getPlayerList().getPlayerByName(nickname);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (targetPlayer.hasDisconnected()) {
            return PrivateMessageResponse.playerNotOnline();
        }
        MutableComponent mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        mutableComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));

        targetPlayer.sendSystemMessage(mutableComponent);

        return PrivateMessageResponse.sendSuccess(getNeoForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.connection.send(packet);
        }
    }
}