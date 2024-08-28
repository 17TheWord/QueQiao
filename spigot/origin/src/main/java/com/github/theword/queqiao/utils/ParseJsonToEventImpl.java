package com.github.theword.queqiao.utils;


import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

// IF >= spigot-1.13
//import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
//import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;
//import net.md_5.bungee.api.chat.hover.content.Entity;
//import net.md_5.bungee.api.chat.hover.content.Item;
//import net.md_5.bungee.api.chat.hover.content.Text;
// END IF

import java.util.List;

import static com.github.theword.queqiao.tool.utils.Tool.logger;


public class ParseJsonToEventImpl implements ParseJsonToEventService {

    /**
     * 将 CommonBaseComponent 转换为 TextComponent
     *
     * @param myBaseComponent 消息体
     * @return TextComponent
     */
    @Override
    public TextComponent parsePerMessageToComponent(CommonTextComponent myBaseComponent) {
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

        if (myBaseComponent.getClickEvent() != null) {
            ClickEvent clickEvent = getClickEvent(myBaseComponent);
            msgComponent.setClickEvent(clickEvent);
        }

        if (myBaseComponent.getHoverEvent() != null) {
            HoverEvent hoverEvent = getHoverEvent(myBaseComponent);
            msgComponent.setHoverEvent(hoverEvent);
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
//        TextComponent textComponent = parseMessageListToComponent(myTextComponent.getHoverEvent().getText());
//        return new HoverEvent(action, new TextComponent[]{textComponent});
        // ELSE
//        HoverEvent hoverEvent = null;
//        switch (action) {
//            case SHOW_TEXT:
//                TextComponent textComponent = parseMessageListToComponent(myTextComponent.getHoverEvent().getText());
//                BaseComponent[] baseComponent = new BaseComponent[]{textComponent};
//                hoverEvent = new HoverEvent(action, new Text(baseComponent));
//                break;
//            case SHOW_ITEM:
//                CommonHoverItem myHoverItem = myTextComponent.getHoverEvent().getItem();
//                ItemTag itemTag = ItemTag.ofNbt(myHoverItem.getTag());
//                Item item = new Item(String.valueOf(myHoverItem.getId()), myHoverItem.getCount(), itemTag);
//                hoverEvent = new HoverEvent(action, item);
//                break;
//            case SHOW_ENTITY:
//                CommonHoverEntity myHoverEntity = myTextComponent.getHoverEvent().getEntity();
//                TextComponent nameComponent = parseMessageListToComponent(myHoverEntity.getName());
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
        ClickEvent.Action action = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction().toUpperCase());
        return new ClickEvent(action, myTextComponent.getClickEvent().getValue());
    }

    /**
     * 将 CommonBaseComponent 转换为 TextComponent
     *
     * @param commonTextComponentList 消息列表
     * @return TextComponent
     */
    @Override
    public TextComponent parseMessageListToComponent(List<MessageSegment> commonTextComponentList) {
        TextComponent component = new TextComponent();
        StringBuilder msgLogText = new StringBuilder();

        for (MessageSegment messageSegment : commonTextComponentList) {
            TextComponent msgComponent = parsePerMessageToComponent(messageSegment.getData());
            component.addExtra(msgComponent);
            msgLogText.append(messageSegment.getData().getText());
        }
        logger.info(msgLogText.toString());
        return component;
    }
}