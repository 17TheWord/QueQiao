package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.utils.NeoForgeTool;
import com.google.gson.JsonElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.NeoForgeTool.getNeoForgePlayer;


public class HandleApiImpl implements HandleApiService {

    /**
     * 广播消息
     *
     * @param jsonElement message
     */
    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        MutableComponent mutableComponent = NeoForgeTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        Component message = NeoForgeTool.buildComponent(jsonElement);
        mutableComponent.append(message);
        sendPacket(new ClientboundSystemChatPacket(mutableComponent, false));
    }

    /**
     * 广播 Send Title 消息
     *
     * @param titleJsonElement    title message
     * @param subtitleJsonElement subtitle message
     * @param fadein              fadein time
     * @param stay                stay time
     * @param fadeout             fadeout time
     */
    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        sendPacket(new ClientboundSetTitlesAnimationPacket(fadein, stay, fadeout));
        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
            MutableComponent title = NeoForgeTool.buildComponent(titleJsonElement);
            sendPacket(new ClientboundSetTitleTextPacket(title));
        }
        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
            MutableComponent subtitle = NeoForgeTool.buildComponent(subtitleJsonElement);
            sendPacket(new ClientboundSetSubtitleTextPacket(subtitle));
        }
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param jsonElement Action Bar message
     */
    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        MutableComponent mutableComponent = NeoForgeTool.buildComponent(jsonElement);
        sendPacket(new ClientboundSetActionBarTextPacket(mutableComponent));
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param jsonElement message
     * @return PrivateMessageResponse
     */
    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
        ServerPlayer targetPlayer;
        if (uuid != null)
            targetPlayer = minecraftServer.getPlayerList().getPlayer(uuid);
        else if (nickname != null && !nickname.isEmpty())
            targetPlayer = minecraftServer.getPlayerList().getPlayerByName(nickname);
        else return PrivateMessageResponse.playerNotFound();

        if (targetPlayer == null) return PrivateMessageResponse.playerIsNull();

        if (targetPlayer.hasDisconnected()) return PrivateMessageResponse.playerNotOnline();

        MutableComponent mutableComponent = NeoForgeTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        MutableComponent message = NeoForgeTool.buildComponent(jsonElement);
        mutableComponent.append(message);
        targetPlayer.sendSystemMessage(mutableComponent);

        return PrivateMessageResponse.sendSuccess(getNeoForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.connection.send(packet);
        }
    }
}