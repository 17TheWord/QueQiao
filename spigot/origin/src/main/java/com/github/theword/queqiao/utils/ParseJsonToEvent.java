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

import static com.github.theword.queqiao.tool.utils.Tool.logger;


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
        else msgComponent.setColor(ChatColor.WHITE);
        msgComponent.setBold(myBaseComponent.isBold());
        msgComponent.setItalic(myBaseComponent.isItalic());
        msgComponent.setUnderlined(myBaseComponent.isUnderlined());
        msgComponent.setStrikethrough(myBaseComponent.isStrikethrough());
        msgComponent.setObfuscated(myBaseComponent.isObfuscated());

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
        HoverEvent.Action action = HoverEvent.Action.valueOf(myTextComponent.getHoverEvent().getAction());
        // IF spigot-1.12.2
//        TextComponent textComponent = parseMessageToTextComponent(myTextComponent.getHoverEvent().getBaseComponentList());
//        return new HoverEvent(action, new TextComponent[]{textComponent});
        // ELSE
//        HoverEvent hoverEvent = null;
//        switch (myTextComponent.getHoverEvent().getAction()) {
//            case "show_text":
//                TextComponent textComponent = parseMessageToTextComponent(myTextComponent.getHoverEvent().getBaseComponentList());
//                BaseComponent[] baseComponent = new BaseComponent[]{textComponent};
//                hoverEvent = new HoverEvent(action, new Text(baseComponent));
//                break;
//            case "show_item":
//                CommonHoverItem myHoverItem = myTextComponent.getHoverEvent().getItem();
//                ItemTag itemTag = ItemTag.ofNbt(myHoverItem.getTag());
//                Item item = new Item(String.valueOf(myHoverItem.getId()), myHoverItem.getCount(), itemTag);
//                hoverEvent = new HoverEvent(action, item);
//                break;
//            case "show_entity":
//                CommonHoverEntity myHoverEntity = myTextComponent.getHoverEvent().getEntity();
//                TextComponent nameComponent = parseMessageToTextComponent(myHoverEntity.getName());
//                Entity entity = new Entity(myHoverEntity.getType(), myHoverEntity.getId(), nameComponent);
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
        ClickEvent.Action action = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction());
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
        logger.info(msgLogText.toString());
        return component;
    }

    /**
     * 将 CommonBaseComponent 转换为带有游戏文本样式的 String
     *
     * @param myBaseComponentList 消息列表
     * @return String
     */
    public String parseCommonBaseCommentToStringWithStyle(List<? extends CommonBaseComponent> myBaseComponentList) {
        StringBuilder message = new StringBuilder();

        for (CommonBaseComponent myBaseComponent : myBaseComponentList) {
            String tempMessageSeg = "";
            if (myBaseComponent.isBold()) tempMessageSeg += ChatColor.BOLD;
            if (myBaseComponent.isItalic()) tempMessageSeg += ChatColor.ITALIC;
            if (myBaseComponent.isUnderlined()) tempMessageSeg += ChatColor.UNDERLINE;
            if (myBaseComponent.isStrikethrough()) tempMessageSeg += ChatColor.STRIKETHROUGH;
            if (myBaseComponent.isObfuscated()) tempMessageSeg += ChatColor.MAGIC;
            if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
                // IF spigot-1.12.2
//                tempMessageSeg += ChatColor.valueOf(myBaseComponent.getColor().toUpperCase());
                // ELSE
//                tempMessageSeg += ChatColor.of(myBaseComponent.getColor().toUpperCase());
                // END IF
            else tempMessageSeg += ChatColor.WHITE;
            tempMessageSeg += myBaseComponent.getText();
            message.append(tempMessageSeg);
        }
        return message.toString();
    }
}