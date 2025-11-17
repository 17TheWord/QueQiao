package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.FabricTool;
import com.google.gson.JsonElement;

import java.util.UUID;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

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

// IF <= fabric-1.18.2
//import net.minecraft.network.MessageType;
// END IF

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

public class HandleApiImpl implements HandleApiService {

    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        MutableText mutableText = FabricTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        assert mutableText != null;
        mutableText.append(FabricTool.buildComponent(jsonElement));

        // IF >= fabric-1.19.2
//        sendPacket(new GameMessageS2CPacket(mutableText, false));
        // ELSE
//        sendPacket(new GameMessageS2CPacket(mutableText, MessageType.CHAT, UUID.randomUUID()));
        // END IF
    }

    @Override
    public void handleSendTitleMessage(JsonElement titleJsonElement, JsonElement subtitleJsonElement, int fadein, int stay, int fadeout) {
        // IF fabric-1.16.5
//        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
//            sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, FabricTool.buildComponent(titleJsonElement), fadein, stay, fadeout));
//        }
//        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
//            sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, FabricTool.buildComponent(subtitleJsonElement), fadein, stay, fadeout));
//        }
        // ELSE
//        sendPacket(new TitleFadeS2CPacket(fadein, stay, fadeout));
//        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
//            sendPacket(new TitleS2CPacket(FabricTool.buildComponent(titleJsonElement)));
//        }
//        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
//            sendPacket(new SubtitleS2CPacket(FabricTool.buildComponent(subtitleJsonElement)));
//        }
        // END IF
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        MutableText mutableText = FabricTool.buildComponent(jsonElement);

        // IF >= fabric-1.19.2
//        sendPacket(new GameMessageS2CPacket(mutableText, false));
        // ELSE
//        sendPacket(new GameMessageS2CPacket(mutableText, MessageType.CHAT, UUID.randomUUID()));
        // END IF
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String s, UUID uuid, JsonElement jsonElement) {
        ServerPlayerEntity serverPlayerEntity;
        if (uuid != null)
            serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(uuid);
        else if (s != null && !s.isEmpty())
            serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(s);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (serverPlayerEntity == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (serverPlayerEntity.isDisconnected()) {
            return PrivateMessageResponse.playerNotOnline();
        }

        MutableText mutableText = FabricTool.buildComponent(GlobalContext.getMessagePrefixJsonObject());
        assert mutableText != null;
        mutableText.append(FabricTool.buildComponent(jsonElement));

        serverPlayerEntity.sendMessage(mutableText, false);
        return PrivateMessageResponse.sendSuccess(getFabricPlayer(serverPlayerEntity));
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(packet);
        }
    }
}