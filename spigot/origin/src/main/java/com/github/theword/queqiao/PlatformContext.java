package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.SpigotTool;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.constant.ServerTypeConstant;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.platform.AbstractPlatformContext;
import io.github.theword.queqiao.core.platform.PlatformResult;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Spigot 平台上下文：core 与 Spigot 服务端之间的唯一接入点。
 *
 * <p>Spigot 没有结构化消息组件，{@code C} 取 Bungee 的 {@code BaseComponent[]}。
 */
public class PlatformContext extends AbstractPlatformContext<Server, BaseComponent[], Player> {

    private volatile QueQiaoRuntime runtime;

    public PlatformContext(Server server) {
        super(server);
    }

    public void bindRuntime(QueQiaoRuntime runtime) {
        this.runtime = runtime;
    }

    public QueQiaoRuntime getRuntime() {
        return runtime;
    }

    @Override
    public String getServerType() {
        return ServerTypeConstant.SPIGOT;
    }

    @Override
    public String getServerVersion() {
        return server.getVersion();
    }

    @Override
    public BaseComponent[] jsonToComponent(JsonElement json) {
        return SpigotTool.buildComponent(json);
    }

    @Override
    public Collection<Player> getPlayers() {
        return new ArrayList<>(server.getOnlinePlayers());
    }

    @Override
    public String getPlayerName(Player player) {
        return player.getName();
    }

    @Override
    public UUID getPlayerUUID(Player player) {
        return player.getUniqueId();
    }

    @Override
    public PlayerModel getPlayerModel(Player player) {
        return SpigotTool.getSpigotPlayer(player);
    }

    @Override
    public PlatformResult<Void> broadcast(BaseComponent[] component) {
        server.spigot().broadcast(withPrefix(component));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(Player player, BaseComponent[] component) {
        player.sendMessage(TextComponent.toLegacyText(withPrefix(component)));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(BaseComponent[] title, BaseComponent[] subtitle,
                                                   int fadeIn, int stay, int fadeOut) {
        String titleText = title == null ? "" : TextComponent.toLegacyText(title);
        String subtitleText = subtitle == null ? "" : TextComponent.toLegacyText(subtitle);

        for (Player player : server.getOnlinePlayers()) {
            player.sendTitle(titleText, subtitleText, fadeIn, stay, fadeOut);
        }
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(BaseComponent[] component) {
        for (Player player : server.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }
        return PlatformResult.success(null);
    }

    /**
     * 按配置拼接消息前缀（core 不再代平台拼接）。
     */
    private BaseComponent withPrefix(BaseComponent[] component) {
        BaseComponent textComponent = new TextComponent();

        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        if (prefix != null) {
            BaseComponent[] prefixComponents = SpigotTool.buildComponent(prefix);
            if (prefixComponents.length > 0) {
                textComponent.addExtra(prefixComponents[0]);
            }
        }

        for (BaseComponent base : component) {
            textComponent.addExtra(base);
        }
        return textComponent;
    }
}
