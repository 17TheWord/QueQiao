package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.utils.ForgeTool;
import com.google.gson.JsonElement;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;


public class HandleApiImpl implements HandleApiService {

    /**
     * 广播消息
     *
     * @param jsonElement 消息体
     */
    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        MutableComponent mutableComponent = ForgeTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        MutableComponent message = ForgeTool.buildComponent(jsonElement);
        mutableComponent.append(message);
        sendPacket(new ClientboundSystemChatPacket(mutableComponent, false));
    }

    /**
     * 广播 Send Title 消息
     *
     * @param titleJsonElement    Title 消息体
     * @param subtitleJsonElement Subtitle 消息体
     * @param fadein              Title 淡入时间
     * @param stay                Title 停留时间
     * @param fadeout             Title 淡出时间
     */
    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        sendPacket(new ClientboundSetTitlesAnimationPacket(fadein, stay, fadeout));
        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
            MutableComponent title = ForgeTool.buildComponent(titleJsonElement);
            sendPacket(new ClientboundSetSubtitleTextPacket(title));
        }
        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
            MutableComponent subtitle = ForgeTool.buildComponent(subtitleJsonElement);
            sendPacket(new ClientboundSetSubtitleTextPacket(subtitle));
        }
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        MutableComponent actionBar = ForgeTool.buildComponent(jsonElement);
        sendPacket(new ClientboundSetActionBarTextPacket(actionBar));
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
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
        MutableComponent mutableComponent = ForgeTool.buildComponent(jsonElement);

        targetPlayer.sendSystemMessage(mutableComponent);

        return PrivateMessageResponse.sendSuccess(getForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.connection.send(packet);
        }
    }
}