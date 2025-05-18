package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;


public class HandleApiImpl implements HandleApiService {
    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    /**
     * 广播消息
     *
     * @param messageList 消息体
     */
    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        TextComponentString mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        mutableComponent.appendSibling(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        for (EntityPlayerMP serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.sendMessage(mutableComponent);
        }
    }


    /**
     * 广播 Send Title 消息
     *
     * @param titlePayload Send Title 消息体
     */
    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        sendPacket(new SPacketTitle(
                SPacketTitle.Type.TITLE,
                parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle()),
                titlePayload.getFadein(),
                titlePayload.getStay(),
                titlePayload.getFadeout()
        ));
        if (titlePayload.getSubtitle() != null)
            sendPacket(new SPacketTitle(
                    SPacketTitle.Type.SUBTITLE,
                    parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle()),
                    titlePayload.getFadein(),
                    titlePayload.getStay(),
                    titlePayload.getFadeout()
            ));
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param messageList Action Bar 消息体
     */
    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        sendPacket(new SPacketTitle(SPacketTitle.Type.ACTIONBAR, parseJsonToEventImpl.parseMessageListToComponent(messageList)));
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
        EntityPlayerMP targetPlayer;
        if (uuid != null)
            targetPlayer = minecraftServer.getPlayerList().getPlayerByUUID(uuid);
        else if (nickname != null && !nickname.isEmpty())
            targetPlayer = minecraftServer.getPlayerList().getPlayerByUsername(nickname);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (targetPlayer.hasDisconnected()) {
            return PrivateMessageResponse.playerNotOnline();
        }
        TextComponentString mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        mutableComponent.appendSibling(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        targetPlayer.sendMessage(mutableComponent);
        return PrivateMessageResponse.sendSuccess(getForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet<?> packet) {
        for (EntityPlayerMP serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.connection.sendPacket(packet);
        }
    }
}