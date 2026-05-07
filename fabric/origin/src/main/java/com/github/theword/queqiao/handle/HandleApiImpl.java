package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.utils.FabricTool;
import com.google.gson.JsonElement;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

public class HandleApiImpl implements HandleApiService {

    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        MutableComponent mutableText = FabricTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        mutableText.append(FabricTool.buildComponent(jsonElement));
        sendPacket(new ClientboundSystemChatPacket(mutableText, false));
    }

    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        sendPacket(new ClientboundSetTitlesAnimationPacket(fadein, stay, fadeout));
        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
            sendPacket(new ClientboundSetTitleTextPacket(FabricTool.buildComponent(titleJsonElement)));
        }
        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
            sendPacket(new ClientboundSetSubtitleTextPacket(FabricTool.buildComponent(subtitleJsonElement)));
        }
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        MutableComponent mutableText = FabricTool.buildComponent(jsonElement);
        sendPacket(new ClientboundSetActionBarTextPacket(mutableText));
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String username, UUID uuid, JsonElement jsonElement) {
        ServerPlayer serverPlayer;
        if (uuid != null) serverPlayer = minecraftServer.getPlayerList().getPlayer(uuid);
        else if (username != null && !username.isEmpty())
            serverPlayer = minecraftServer.getPlayerList().getPlayer(username);
        else return PrivateMessageResponse.playerNotFound();

        if (serverPlayer == null) return PrivateMessageResponse.playerIsNull();

        if (serverPlayer.hasDisconnected()) return PrivateMessageResponse.playerNotOnline();

        MutableComponent mutableText = FabricTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        mutableText.append(FabricTool.buildComponent(jsonElement));

        serverPlayer.sendSystemMessage(mutableText, false);
        return PrivateMessageResponse.sendSuccess(getFabricPlayer(serverPlayer));
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayer player : minecraftServer.getPlayerList().getPlayers()) {
            player.connection.send(packet);
        }
    }
}