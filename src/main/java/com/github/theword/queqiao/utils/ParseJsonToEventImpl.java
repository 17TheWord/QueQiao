package com.github.theword.queqiao.utils;

import java.util.List;

import com.github.theword.queqiao.tool.GlobalContext;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;

public class ParseJsonToEventImpl implements ParseJsonToEventService {

    @Override
    public ChatComponentText parseMessageListToComponent(List<MessageSegment> myBaseComponentList) {
        ChatComponentText mutableComponent = new ChatComponentText("");
        StringBuilder msgLogText = new StringBuilder();
        for (MessageSegment messageSegment : myBaseComponentList) {
            ChatComponentText tempMutableComponent = parsePerMessageToComponent(messageSegment.getData());
            mutableComponent.appendSibling(tempMutableComponent);
            msgLogText.append(
                messageSegment.getData()
                    .getText());
        }
        GlobalContext.getLogger().info(msgLogText.toString());
        return mutableComponent;
    }

    @Override
    public ChatComponentText parseCommonBaseComponentListToComponent(List<CommonBaseComponent> myBaseComponentList) {

        ChatComponentText mutableComponent = new ChatComponentText("");
        for (CommonBaseComponent commonBaseComponent : myBaseComponentList) {
            ChatComponentText tempMutableComponent = parsePerMessageToComponent(commonBaseComponent);
            mutableComponent.appendSibling(tempMutableComponent);
        }
        return mutableComponent;
    }

    @Override
    public ChatComponentText parsePerMessageToComponent(CommonBaseComponent myBaseComponent) {

        ChatComponentText tempMutableComponent = new ChatComponentText(myBaseComponent.getText());

        ChatStyle style = getStyleFromBaseComponent(myBaseComponent);

        if (myBaseComponent instanceof CommonTextComponent commonTextComponent) {
            if (commonTextComponent.getClickEvent() != null) {
                ClickEvent clickEventFromBaseComponent = getClickEventFromBaseComponent(commonTextComponent);
                if (clickEventFromBaseComponent != null) style = style.setChatClickEvent(clickEventFromBaseComponent);
            }
            if (commonTextComponent.getHoverEvent() != null) {
                HoverEvent hoverEventFromBaseComponent = getHoverEventFromBaseComponent(commonTextComponent);
                if (hoverEventFromBaseComponent != null) style = style.setChatHoverEvent(hoverEventFromBaseComponent);
            }
        }

        tempMutableComponent.setChatStyle(style);
        return tempMutableComponent;
    }

    private ChatStyle getStyleFromBaseComponent(CommonBaseComponent myBaseComponent) {
        ChatStyle style = new ChatStyle().setBold(myBaseComponent.isBold())
            .setItalic(myBaseComponent.isItalic())
            .setUnderlined(myBaseComponent.isUnderlined())
            .setStrikethrough(myBaseComponent.isStrikethrough())
            .setObfuscated(myBaseComponent.isObfuscated());
        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor()
            .isEmpty()) style.setColor(
                EnumChatFormatting.getValueByName(
                    myBaseComponent.getColor()
                        .toUpperCase()));
        else style.setColor(EnumChatFormatting.WHITE);
        return style;
    }

    private ClickEvent getClickEventFromBaseComponent(CommonTextComponent myTextComponent) {
        if (myTextComponent.getClickEvent() != null) {
            ClickEvent.Action tempAction = ClickEvent.Action.valueOf(
                myTextComponent.getClickEvent()
                    .getAction()
                    .toLowerCase());
            return new ClickEvent(
                tempAction,
                myTextComponent.getClickEvent()
                    .getValue());
        }
        return null;
    }

    public HoverEvent getHoverEventFromBaseComponent(CommonTextComponent myTextComponent) {
        HoverEvent hoverEvent = null;
        HoverEvent.Action action = HoverEvent.Action.valueOf(
            myTextComponent.getHoverEvent()
                .getAction()
                .toLowerCase());
        if (action.equals(HoverEvent.Action.SHOW_TEXT)) {
            if (myTextComponent.getHoverEvent()
                .getText() != null
                && !myTextComponent.getHoverEvent()
                    .getText()
                    .isEmpty()) {
                ChatComponentText textComponent = parseCommonBaseComponentListToComponent(
                    myTextComponent.getHoverEvent()
                        .getText());
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
            }
        } else if (action.equals(HoverEvent.Action.SHOW_ITEM)) {
            CommonHoverItem commonHoverItem = myTextComponent.getHoverEvent()
                .getItem();
            Item item = Item.getItemById(Integer.parseInt(commonHoverItem.getId()));
            ItemStack itemStack = new ItemStack(item, commonHoverItem.getCount());
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(itemStack.getDisplayName()));
        } else if (action.equals(HoverEvent.Action.SHOW_ACHIEVEMENT)) {
            CommonHoverEntity commonHoverEntity = myTextComponent.getHoverEvent()
                .getEntity();
            hoverEvent = new HoverEvent(
                HoverEvent.Action.SHOW_ACHIEVEMENT,
                parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
        }
        return hoverEvent;
    }
}
