package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.google.gson.JsonElement;

import java.util.UUID;

// IF > forge-1.16.5
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.*;
//import net.minecraft.server.level.ServerPlayer;
// ELSE
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.network.IPacket;
//import net.minecraft.network.play.server.SChatPacket;
//import net.minecraft.network.play.server.STitlePacket;
//import net.minecraft.util.text.ChatType;
//import net.minecraft.util.text.ITextComponent;
// END IF

// IF forge-1.18.2
//import net.minecraft.network.chat.ChatType;
// END IF

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
        // IF >= forge-1.21
//        MutableComponent mutableComponent = Component.Serializer.fromJson(Tool.getPrefixComponent(), minecraftServer.registryAccess());
//        assert mutableComponent != null;
//        MutableComponent message = Component.Serializer.fromJson(jsonElement, minecraftServer.registryAccess());
//        if (message != null) {
//            mutableComponent.append(message);
//        }
//        sendPacket(new ClientboundSystemChatPacket(mutableComponent, false));
        // ELSE IF >= forge-1.18
//        MutableComponent mutableComponent = Component.Serializer.fromJson(Tool.getPrefixComponent());
//        assert mutableComponent != null;
//        MutableComponent message = Component.Serializer.fromJson(jsonElement);
//        if (message != null) {
//            mutableComponent.append(message);
//        }
        // IF >= forge-1.19
//        sendPacket(new ClientboundSystemChatPacket(mutableComponent, false));
        // ELSE
//        sendPacket(new ClientboundChatPacket(mutableComponent, ChatType.CHAT, UUID.randomUUID()));
        // END IF
        // ELSE
//        net.minecraft.util.text.IFormattableTextComponent mutableComponent = ITextComponent.Serializer.fromJson(Tool.getPrefixComponent());
//        assert mutableComponent != null;
//        net.minecraft.util.text.IFormattableTextComponent messageComponent = ITextComponent.Serializer.fromJson(jsonElement);
//        if (messageComponent != null) {
//            mutableComponent.append(messageComponent);
//        }
//        sendPacket(new SChatPacket(mutableComponent, ChatType.CHAT, UUID.randomUUID()));
        // END IF
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
        // IF >= forge-1.21
//        sendPacket(new ClientboundSetTitlesAnimationPacket(fadein, stay, fadeout));
//        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
//            MutableComponent title = Component.Serializer.fromJson(titleJsonElement, minecraftServer.registryAccess());
//            if (title != null)
//                sendPacket(new ClientboundSetSubtitleTextPacket(title));
//        }
//        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
//            MutableComponent subtitle = Component.Serializer.fromJson(subtitleJsonElement, minecraftServer.registryAccess());
//            if (subtitle != null)
//                sendPacket(new ClientboundSetSubtitleTextPacket(subtitle));
//        }
        // ELSE IF >= forge-1.18
//        sendPacket(new ClientboundSetTitlesAnimationPacket(fadein, stay, fadeout));
//        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
//            MutableComponent title = Component.Serializer.fromJson(titleJsonElement);
//            if (title != null)
//                sendPacket(new ClientboundSetSubtitleTextPacket(title));
//        }
//
//        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
//            MutableComponent subtitle = Component.Serializer.fromJson(subtitleJsonElement);
//            if (subtitle != null)
//                sendPacket(new ClientboundSetSubtitleTextPacket(subtitle));
//        }
        // ELSE
//        sendPacket(new STitlePacket(fadein, stay, fadeout));
//        if (titleJsonElement != null && !titleJsonElement.isJsonNull()) {
//            net.minecraft.util.text.IFormattableTextComponent title = ITextComponent.Serializer.fromJson(titleJsonElement);
//            if (title != null)
//                sendPacket(new STitlePacket(STitlePacket.Type.TITLE, title));
//        }
//
//        if (subtitleJsonElement != null && !subtitleJsonElement.isJsonNull()) {
//            net.minecraft.util.text.IFormattableTextComponent subtitle = ITextComponent.Serializer.fromJson(subtitleJsonElement);
//            if (subtitle != null)
//                sendPacket(new STitlePacket(STitlePacket.Type.SUBTITLE, subtitle));
//        }
        // END IF
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        // IF >= forge-1.21
//        MutableComponent actionBar = Component.Serializer.fromJson(jsonElement, minecraftServer.registryAccess());
//        if (actionBar != null) {
//            sendPacket(new ClientboundSetActionBarTextPacket(actionBar));
//        }
        // ELSE IF >= forge-1.18
//        MutableComponent actionBar = Component.Serializer.fromJson(jsonElement);
//        if (actionBar != null) {
//            sendPacket(new ClientboundSetActionBarTextPacket(actionBar));
//        }
        // ELSE
//        ITextComponent actionBar = ITextComponent.Serializer.fromJson(jsonElement);
//        if (actionBar != null) {
//            sendPacket(new SChatPacket(actionBar, ChatType.GAME_INFO, UUID.randomUUID()));
//        }
        // END IF
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
        // IF > forge-1.16.5
//        ServerPlayer targetPlayer;
        // ELSE
//        ServerPlayerEntity targetPlayer;
        // END IF
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
        // IF >= forge-1.21
//        MutableComponent mutableComponent = Component.Serializer.fromJson(jsonElement, minecraftServer.registryAccess());
        // ELSE IF >= forge-1.18
//        MutableComponent mutableComponent = Component.Serializer.fromJson(jsonElement);
        // ELSE
//        ITextComponent mutableComponent = ITextComponent.Serializer.fromJson(jsonElement);
        // END IF

        if (mutableComponent == null) {
            return PrivateMessageResponse.of(getForgePlayer(targetPlayer), "消息内容错误");
        }

        // IF >= forge-1.19
//        targetPlayer.sendSystemMessage(mutableComponent);
        // ELSE
//        targetPlayer.sendMessage(mutableComponent, UUID.randomUUID());
        // END IF

        return PrivateMessageResponse.sendSuccess(getForgePlayer(targetPlayer));
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