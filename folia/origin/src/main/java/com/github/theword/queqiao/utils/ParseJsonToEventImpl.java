package com.github.theword.queqiao.utils;


import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;

import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.intellij.lang.annotations.Subst;

import java.util.List;
import java.util.UUID;

import static com.github.theword.queqiao.tool.utils.Tool.logger;


public class ParseJsonToEventImpl implements ParseJsonToEventService {

    /**
     * 将 CommonBaseComponent 转换为 TextComponent
     *
     * @param commonBaseComponent 消息体
     * @return TextComponent
     */
    @Override
    public TextComponent parsePerMessageToComponent(CommonBaseComponent commonBaseComponent) {
        TextComponent.Builder msgComponent = Component.text();

        // 配置 BaseComponent 基本属性
        msgComponent.content(commonBaseComponent.getText());
        msgComponent.style(getStyle(commonBaseComponent));

        if (commonBaseComponent instanceof CommonTextComponent commonTextComponent) {
            if (commonTextComponent.getClickEvent() != null) {
                ClickEvent clickEvent = getClickEvent(commonTextComponent);
                msgComponent.clickEvent(clickEvent);
            }

            if (commonTextComponent.getHoverEvent() != null) {
                HoverEvent<?> hoverEvent = getHoverEvent(commonTextComponent);
                msgComponent.hoverEvent(hoverEvent);
            }
        }
        return msgComponent.build();
    }

    private Style getStyle(CommonBaseComponent commonBaseComponent) {
        Style style = Style.empty();
        if (commonBaseComponent.getColor() != null && !commonBaseComponent.getColor().isEmpty()) {
            NamedTextColor value = NamedTextColor.NAMES.value(commonBaseComponent.getColor().toUpperCase());
            style = style.color(value);
        } else style = style.color(NamedTextColor.WHITE);
        if (commonBaseComponent.isBold()) style = style.decorate(TextDecoration.BOLD);
        if (commonBaseComponent.isItalic()) style = style.decorate(TextDecoration.ITALIC);
        if (commonBaseComponent.isUnderlined()) style = style.decorate(TextDecoration.UNDERLINED);
        if (commonBaseComponent.isStrikethrough()) style = style.decorate(TextDecoration.STRIKETHROUGH);
        if (commonBaseComponent.isObfuscated()) style = style.decorate(TextDecoration.OBFUSCATED);
        if (commonBaseComponent.getFont() != null && !commonBaseComponent.getFont().isEmpty()) {
            style = style.font(Key.key(commonBaseComponent.getFont()));
        }
        return style;
    }


    /**
     * 获取 HoverEvent
     *
     * @param myTextComponent Text消息体
     * @return HoverEvent
     */
    private HoverEvent<?> getHoverEvent(@Subst("") CommonTextComponent myTextComponent) {
        HoverEvent<?> hoverEvent = null;
        switch (myTextComponent.getHoverEvent().getAction().toUpperCase()) {
            case "SHOW_TEXT":
                TextComponent textComponent = parseCommonBaseComponentListToComponent(myTextComponent.getHoverEvent().getText());
                hoverEvent = HoverEvent.showText(textComponent);
                break;
            case "SHOW_ITEM":
                CommonHoverItem commonHoverItem = myTextComponent.getHoverEvent().getItem();
                Key key = Key.key(commonHoverItem.getKey());
                hoverEvent = HoverEvent.showItem(key, commonHoverItem.getCount());
                break;
            case "SHOW_ENTITY":
                CommonHoverEntity commonHoverEntity = myTextComponent.getHoverEvent().getEntity();
                if (commonHoverEntity.getName() != null) {
                    TextComponent nameComponent = parseCommonBaseComponentListToComponent(commonHoverEntity.getName());
                    hoverEvent = HoverEvent.showEntity(Key.key(commonHoverEntity.getKey()), UUID.randomUUID(), nameComponent);
                } else {
                    hoverEvent = HoverEvent.showEntity(Key.key(commonHoverEntity.getKey()), UUID.randomUUID());
                }
                break;
            default:
                break;
        }
        return hoverEvent;
    }

    /**
     * 获取 ClickEvent
     *
     * @param myTextComponent Text消息体
     * @return ClickEvent
     */
    private ClickEvent getClickEvent(CommonTextComponent myTextComponent) {
        ClickEvent.Action action = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction().toUpperCase());
        return ClickEvent.clickEvent(action, myTextComponent.getClickEvent().getValue());
    }

    /**
     * 将 CommonBaseComponent 转换为 TextComponent
     *
     * @param commonTextComponentList 消息列表
     * @return TextComponent
     */
    @Override
    public TextComponent parseMessageListToComponent(List<MessageSegment> commonTextComponentList) {
        TextComponent.Builder component = Component.text();
        StringBuilder msgLogText = new StringBuilder();

        for (MessageSegment messageSegment : commonTextComponentList) {
            TextComponent msgComponent = parsePerMessageToComponent(messageSegment.getData());
            component.append(msgComponent);
            msgLogText.append(messageSegment.getData().getText());
        }
        logger.info(msgLogText.toString());
        return component.build();
    }

    @Override
    public TextComponent parseCommonBaseComponentListToComponent(List<CommonBaseComponent> list) {
        TextComponent.Builder component = Component.text();
        for (CommonBaseComponent commonBaseComponent : list) {
            TextComponent msgComponent = parsePerMessageToComponent(commonBaseComponent);
            component.append(msgComponent);
        }
        return component.build();
    }
}