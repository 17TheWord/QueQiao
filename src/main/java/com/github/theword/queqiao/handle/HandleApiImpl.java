package com.github.theword.queqiao.handle;

import static com.github.theword.queqiao.QueQiao.minecraftServer;
import static com.github.theword.queqiao.utils.ForgeTool.getForgePlayer;

import java.util.List;
import java.util.UUID;

import com.github.theword.queqiao.utils.ForgeTool;
import com.google.gson.JsonElement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import net.minecraft.util.IChatComponent;

public class HandleApiImpl implements HandleApiService {


    @Override
    public void handleBroadcastMessage(JsonElement jsonElement) {
        IChatComponent component = ForgeTool.buildComponent(Tool.getPrefixComponent());
        component.appendSibling(ForgeTool.buildComponent(jsonElement));
        sendPacket(new S02PacketChat(component, true));
    }

    @Override
    public void handleSendTitleMessage(JsonElement titlePayload, JsonElement subTitlePayload, int fadeIn, int stay, int fadeOut) {
        if (!titlePayload.isJsonNull()) {
            sendPacket(new S02PacketChat(ForgeTool.buildComponent(titlePayload), false));
        }
        if (!subTitlePayload.isJsonNull()) {
            sendPacket(new S02PacketChat(ForgeTool.buildComponent(subTitlePayload), false));
        }
    }

    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        sendPacket(new S02PacketChat(IChatComponent.Serializer.func_150699_a(jsonElement.toString()), false));
    }

    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
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

        if (targetPlayer.field_70135_K) {
            return PrivateMessageResponse.playerNotOnline();
        }
        IChatComponent component = ForgeTool.buildComponent(Tool.getPrefixComponent());
        component.appendSibling(ForgeTool.buildComponent(jsonElement));
        targetPlayer.addChatComponentMessage(component);
        return PrivateMessageResponse.sendSuccess(getForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet packet) {
        List<EntityPlayerMP> playerEntityList = minecraftServer.getConfigurationManager().playerEntityList;
        for (EntityPlayerMP player : playerEntityList) {
            player.playerNetServerHandler.sendPacket(packet);
        }
    }
}
