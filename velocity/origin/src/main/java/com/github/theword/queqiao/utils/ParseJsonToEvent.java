package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.intellij.lang.annotations.Subst;


import java.util.List;

public class ParseJsonToEvent {

    public Component parseMessage(List<? extends CommonBaseComponent> commonBaseComponentList) {
        Component component = Component.empty();
        for (CommonBaseComponent commonBaseComponent : commonBaseComponentList) {
            component = component.append(Component.text(commonBaseComponent.getText()));
        }
        return component;
    }

    public Component parsePerMessageToMultiText(CommonBaseComponent commonBaseComponent) {
        Component component = Component.text(commonBaseComponent.getText());

        if (commonBaseComponent.getColor() != null && !commonBaseComponent.getColor().isEmpty())
            component = component.color(getNamedTextColor(commonBaseComponent.getColor()));

        component = getTextStyle(commonBaseComponent, component);

        if (commonBaseComponent instanceof CommonTextComponent commonTextComponent) {
            if (commonTextComponent.getClickEvent() != null)
                component = component.clickEvent(getClickEvent(commonTextComponent));

            if (commonTextComponent.getHoverEvent() != null)
                component = component.hoverEvent(getHoverEvent(commonTextComponent));
        }


        return component;
    }

    private ClickEvent getClickEvent(CommonTextComponent commonTextComponent) {
        ClickEvent.Action action = ClickEvent.Action.valueOf(commonTextComponent.getClickEvent().getAction().toLowerCase());
        return ClickEvent.clickEvent(action, commonTextComponent.getClickEvent().getValue());
    }

    private HoverEvent<?> getHoverEvent(CommonTextComponent commonTextComponent) {
        String action = commonTextComponent.getClickEvent().getAction();
        switch (action) {
            case "show_text" -> {
                return HoverEvent.showText(parseMessage(commonTextComponent.getHoverEvent().getBaseComponentList()));
            }
            case "show_item" -> {
                return HoverEvent.showItem(
                        Key.key(commonTextComponent.getHoverEvent().getItem().getKey()),
                        commonTextComponent.getHoverEvent().getItem().getCount()
                );
            }
            case "show_entity" -> {
                return HoverEvent.showEntity(
                        Key.key(commonTextComponent.getHoverEvent().getEntity().getKey()),
                        commonTextComponent.getHoverEvent().getEntity().getUuid(),
                        parseMessage(commonTextComponent.getHoverEvent().getEntity().getName())
                );
            }
        }
        return null;
    }


    private Component getTextStyle(CommonBaseComponent commonBaseComponent, Component component) {
        if (commonBaseComponent.isBold())
            component = component.decorate(TextDecoration.BOLD);

        if (commonBaseComponent.isItalic())
            component = component.decorate(TextDecoration.ITALIC);

        if (commonBaseComponent.isUnderlined())
            component = component.decorate(TextDecoration.UNDERLINED);

        if (commonBaseComponent.isStrikethrough())
            component = component.decorate(TextDecoration.STRIKETHROUGH);

        if (commonBaseComponent.isObfuscated())
            component = component.decorate(TextDecoration.OBFUSCATED);

        if (commonBaseComponent.getFont() != null && !commonBaseComponent.getFont().isEmpty()) {
            @Subst("default") String font = commonBaseComponent.getFont();
            if (Key.parseable(font))
                component = component.font(Key.key(font));
        }

        return component;
    }

    private NamedTextColor getNamedTextColor(String color) {
        return switch (color.toLowerCase()) {
            case "black" -> NamedTextColor.BLACK;
            case "dark_blue" -> NamedTextColor.DARK_BLUE;
            case "dark_green" -> NamedTextColor.DARK_GREEN;
            case "dark_aqua" -> NamedTextColor.DARK_AQUA;
            case "dark_red" -> NamedTextColor.DARK_RED;
            case "dark_purple" -> NamedTextColor.DARK_PURPLE;
            case "gold" -> NamedTextColor.GOLD;
            case "gray" -> NamedTextColor.GRAY;
            case "dark_gray" -> NamedTextColor.DARK_GRAY;
            case "blue" -> NamedTextColor.BLUE;
            case "green" -> NamedTextColor.GREEN;
            case "aqua" -> NamedTextColor.AQUA;
            case "red" -> NamedTextColor.RED;
            case "light_purple" -> NamedTextColor.LIGHT_PURPLE;
            case "yellow" -> NamedTextColor.YELLOW;
            default -> NamedTextColor.WHITE;
        };
    }
}