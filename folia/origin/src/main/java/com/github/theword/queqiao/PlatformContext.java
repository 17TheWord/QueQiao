package com.github.theword.queqiao;

import com.github.theword.queqiao.utils.FoliaTool;
import com.google.gson.JsonElement;
import io.github.theword.queqiao.core.constant.ServerTypeConstant;
import io.github.theword.queqiao.core.event.model.PlayerModel;
import io.github.theword.queqiao.core.event.model.TranslateModel;
import io.github.theword.queqiao.core.platform.AbstractPlatformContext;
import io.github.theword.queqiao.core.platform.PlatformResult;
import io.github.theword.queqiao.core.runtime.QueQiaoRuntime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Folia 平台上下文：core 与 Folia 服务端之间的唯一接入点。
 */
public class PlatformContext extends AbstractPlatformContext<Server, Component, Player> {

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
    public Component jsonToComponent(JsonElement json) {
        return FoliaTool.buildComponent(json);
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
        return FoliaTool.getFoliaPlayer(player);
    }

    @Override
    public PlatformResult<Void> broadcast(Component component) {
        server.sendMessage(withPrefix(component));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendPrivateMessage(Player player, Component component) {
        player.sendMessage(withPrefix(component));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendTitleComponent(Component title, Component subtitle,
                                                   int fadeIn, int stay, int fadeOut) {
        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
        );
        server.showTitle(Title.title(
                title == null ? Component.empty() : title,
                subtitle == null ? Component.empty() : subtitle,
                times
        ));
        return PlatformResult.success(null);
    }

    @Override
    public PlatformResult<Void> sendActionBarComponent(Component component) {
        server.sendActionBar(component);
        return PlatformResult.success(null);
    }

    /**
     * 把 Adventure 的翻译组件拆成 TranslateModel。
     */
    @Override
    public TranslateModel parseTranslateModel(Component component) {
        if (!(component instanceof TranslatableComponent)) {
            return new TranslateModel(null, null, FoliaTool.getComponentText(component));
        }

        TranslatableComponent translatable = (TranslatableComponent) component;
        List<Component> args = translatable.args();

        TranslateModel[] childModels = new TranslateModel[args.size()];
        String[] formattedArgs = new String[args.size()];
        for (int i = 0; i < args.size(); i++) {
            TranslateModel child = parseTranslateModel(args.get(i));
            childModels[i] = child;
            formattedArgs[i] = child.getText();
        }

        String finalText = getTranslationService().translate(translatable.key(), formattedArgs);
        if (finalText == null || finalText.equals(translatable.key())) {
            finalText = FoliaTool.getComponentText(component);
        } else if (!component.children().isEmpty()) {
            StringBuilder builder = new StringBuilder(finalText);
            for (Component child : component.children()) {
                builder.append(parseTranslateModel(child).getText());
            }
            finalText = builder.toString();
        }

        return new TranslateModel(translatable.key(), childModels, finalText);
    }

    private Component withPrefix(Component component) {
        QueQiaoRuntime current = runtime;
        JsonElement prefix = current == null ? null : current.getMessagePrefixJsonElement();
        if (prefix == null) {
            return component;
        }
        return FoliaTool.buildComponent(prefix).append(component);
    }
}
