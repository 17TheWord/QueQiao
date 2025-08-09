package com.github.theword.queqiao.handle;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.TitlePayload;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ParseJsonToEventImpl;

public class HandleApiImpl implements HandleApiService {

    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    @Override
    public void handleBroadcastMessage(List<MessageSegment> messageList) {
        ChatComponentText mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        mutableComponent.appendSibling(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        sendPacket(new S02PacketChat(mutableComponent, true));
    }

    @Override
    public void handleSendTitleMessage(TitlePayload titlePayload) {
        sendPacket(new S02PacketChat(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getTitle()), false));
        if (titlePayload.getSubtitle() != null) sendPacket(
            new S02PacketChat(parseJsonToEventImpl.parseMessageListToComponent(titlePayload.getSubtitle()), false));
    }

    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        sendPacket(new S02PacketChat(parseJsonToEventImpl.parseMessageListToComponent(messageList), false));
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid,
        List<MessageSegment> messageList) {
        EntityPlayerMP targetPlayer = null;
        if (uuid != null) {
            List<EntityPlayerMP> playerEntityList = minecraftServer.getConfigurationManager().playerEntityList;
            for (EntityPlayerMP player : playerEntityList) {
                if (player.getUniqueID()
                    .equals(uuid)) {
                    targetPlayer = player;
                    break;
                }
            }
        } else if (nickname != null && !nickname.isEmpty()) targetPlayer = minecraftServer.getConfigurationManager()
            .func_152612_a(nickname);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        // if (targetPlayer.field_70135_K) {
        // return PrivateMessageResponse.playerNotOnline();
        // }
        ChatComponentText mutableComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        mutableComponent.appendSibling(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        targetPlayer.addChatComponentMessage(mutableComponent);
        return PrivateMessageResponse.sendSuccess(getForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet packet) {
        List<EntityPlayerMP> playerEntityList = minecraftServer.getConfigurationManager().playerEntityList;
        for (EntityPlayerMP player : playerEntityList) {
            player.playerNetServerHandler.sendPacket(packet);
        }
    }
}
