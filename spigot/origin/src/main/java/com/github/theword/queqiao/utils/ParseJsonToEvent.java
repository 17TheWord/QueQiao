package com.github.theword.queqiao.utils;


import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
// IF spigot-1.12.2
//
// ELSE
//import com.github.theword.queqiao.tool.payload.modle.CommonHoverEntity;
//import com.github.theword.queqiao.tool.payload.modle.CommonHoverItem;
//import net.md_5.bungee.api.chat.hover.content.Entity;
//import net.md_5.bungee.api.chat.hover.content.Item;
//import net.md_5.bungee.api.chat.hover.content.Text;
// END IF

import java.util.List;

import static com.github.theword.queqiao.tool.utils.Tool.debugLog;


public class ParseJsonToEvent {

    /**
     * 将 CommonBaseComponent 转换为 TextComponent
     *
     * @param myBaseComponent 消息体
     * @return TextComponent
     */
    public TextComponent parsePerMessageToTextComponent(CommonBaseComponent myBaseComponent) {
        TextComponent msgComponent = new TextComponent();

        // 配置 BaseComponent 基本属性
        msgComponent.setText(myBaseComponent.getText());
        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
            // IF spigot-1.12.2
//            msgComponent.setColor(ChatColor.valueOf(myBaseComponent.getColor()));
            // ELSE
//            msgComponent.setColor(ChatColor.of(myBaseComponent.getColor().toUpperCase()));
            // END IF

        if (myBaseComponent.isBold()) msgComponent.setBold(true);
        if (myBaseComponent.isItalic()) msgComponent.setItalic(true);
        if (myBaseComponent.isUnderlined()) msgComponent.setUnderlined(true);
        if (myBaseComponent.isStrikethrough()) msgComponent.setStrikethrough(true);
        if (myBaseComponent.isObfuscated()) msgComponent.setObfuscated(true);

        // 配置 TextComponent 额外属性
        if (myBaseComponent instanceof CommonTextComponent) {
            CommonTextComponent myTextComponent = (CommonTextComponent) myBaseComponent;

            if (myTextComponent.getClickEvent() != null) {
                ClickEvent clickEvent = getClickEvent(myTextComponent);
                msgComponent.setClickEvent(clickEvent);
            }

            if (myTextComponent.getHoverEvent() != null) {
                HoverEvent hoverEvent = getHoverEvent(myTextComponent);
                msgComponent.setHoverEvent(hoverEvent);
            }
        }
        return msgComponent;
    }

    /**
     * 获取 HoverEvent
     *
     * @param myTextComponent Text消息体
     * @return HoverEvent
     */
    private HoverEvent getHoverEvent(CommonTextComponent myTextComponent) {
        HoverEvent.Action action = HoverEvent.Action.valueOf(myTextComponent.getHoverEvent().getAction().toUpperCase());
        // IF spigot-1.12.2
//        TextComponent textComponent = parseMessageToTextComponent(myTextComponent.getHoverEvent().getBaseComponentList());
//        return new HoverEvent(action, new TextComponent[]{textComponent});
        // ELSE
//        HoverEvent hoverEvent = null;
//        switch (action) {
//            case SHOW_TEXT:
//                TextComponent textComponent = parseMessageToTextComponent(myTextComponent.getHoverEvent().getBaseComponentList());
//                BaseComponent[] baseComponent = new BaseComponent[]{textComponent};
//                hoverEvent = new HoverEvent(action, new Text(baseComponent));
//                break;
//            case SHOW_ITEM:
//                CommonHoverItem commonHoverItem = myTextComponent.getHoverEvent().getItem();
//                ItemTag itemTag = ItemTag.ofNbt(commonHoverItem.getTag());
//                Item item = new Item(commonHoverItem.getId(), commonHoverItem.getCount(), itemTag);
//                hoverEvent = new HoverEvent(action, item);
//                break;
//            case SHOW_ENTITY:
//                CommonHoverEntity commonHoverEntity = myTextComponent.getHoverEvent().getEntity();
//                TextComponent nameComponent = parseMessageToTextComponent(commonHoverEntity.getName());
//                Entity entity = new Entity(commonHoverEntity.getType(), commonHoverEntity.getId(), nameComponent);
//                hoverEvent = new HoverEvent(action, entity);
//                break;
//            default:
//                break;
//        }
//        return hoverEvent;
        // END IF
    }

    /**
     * 获取 ClickEvent
     *
     * @param myTextComponent Text消息体
     * @return ClickEvent
     */
    private ClickEvent getClickEvent(CommonTextComponent myTextComponent) {
        ClickEvent.Action action = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction().toUpperCase());
        return new ClickEvent(action, myTextComponent.getClickEvent().getValue());
    }

    /**
     * 将 CommonBaseComponent 转换为 TextComponent
     *
     * @param myBaseComponentList 消息列表
     * @return TextComponent
     */
    public TextComponent parseMessageToTextComponent(List<? extends CommonBaseComponent> myBaseComponentList) {
        TextComponent component = new TextComponent();
        StringBuilder msgLogText = new StringBuilder();

        for (CommonBaseComponent myBaseComponent : myBaseComponentList) {
            TextComponent msgComponent = parsePerMessageToTextComponent(myBaseComponent);
            component.addExtra(msgComponent);
            msgLogText.append(myBaseComponent.getText());
        }
        debugLog(msgLogText.toString());
        return component;
    }
}