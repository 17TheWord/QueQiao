package com.github.theword.queqiao.handle;

import com.github.theword.queqiao.tool.handle.HandleApiService;
import com.github.theword.queqiao.tool.response.PrivateMessageResponse;
import com.github.theword.queqiao.tool.utils.Tool;
import com.github.theword.queqiao.utils.ForgeTool;
import com.google.gson.JsonElement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;

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
        ITextComponent prefixed = ForgeTool.parseJsonToTextWrapped(Tool.getPrefixComponent());
        ITextComponent message = ForgeTool.parseJsonToTextWrapped(jsonElement);
        if (message != null && prefixed != null) {
            for (EntityPlayerMP serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
                serverPlayer.sendMessage(prefixed.appendSibling(message));
            }
        }
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

        ITextComponent title = ForgeTool.parseJsonToTextWrapped(titleJsonElement);

        ITextComponent subtitle = ForgeTool.parseJsonToTextWrapped(subtitleJsonElement);

        if(title!=null) {
            sendPacket(new SPacketTitle(
                    SPacketTitle.Type.TITLE,
                    title,
                    fadein,
                    stay,
                    fadeout
            ));
        }
        if (subtitle != null)
            sendPacket(new SPacketTitle(
                    SPacketTitle.Type.SUBTITLE,
                    subtitle,
                    fadein,
                    stay,
                    fadeout
            ));
    }

    /**
     * 广播 Action Bar 消息
     *
     * @param jsonElement Action Bar 消息体
     */
    @Override
    public void handleSendActionBarMessage(JsonElement jsonElement) {
        ITextComponent msg = ForgeTool.parseJsonToTextWrapped(jsonElement);
        if(msg!=null) {
            sendPacket(new SPacketTitle(SPacketTitle.Type.ACTIONBAR, msg));
        }
    }

    /**
     * 私聊消息
     *
     * @param nickname    目标玩家名称
     * @param uuid        目标玩家 UUID
     * @param jsonElement 消息体
     */
    @Override
    public PrivateMessageResponse handleSendPrivateMessage(String nickname, UUID uuid, JsonElement jsonElement) {
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

        ITextComponent prefixed = ForgeTool.parseJsonToTextWrapped(Tool.getPrefixComponent());
        ITextComponent message = ForgeTool.parseJsonToTextWrapped(jsonElement);
        if (message != null && prefixed != null) {
            targetPlayer.sendMessage(prefixed.appendSibling(message));
        }
        return PrivateMessageResponse.sendSuccess(getForgePlayer(targetPlayer));
    }

    private void sendPacket(Packet<?> packet) {
        for (EntityPlayerMP serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.connection.sendPacket(packet);
        }
    }
}