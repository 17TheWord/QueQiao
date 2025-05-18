package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;

import static com.github.theword.queqiao.tool.utils.Tool.logger;

public class ParseJsonToEventImpl implements ParseJsonToEventService {

    @Override
    public TextComponentString parseMessageListToComponent(List<MessageSegment> myBaseComponentList) {
        TextComponentString mutableComponent = new TextComponentString("");
        StringBuilder msgLogText = new StringBuilder();
        for (MessageSegment messageSegment : myBaseComponentList) {
            TextComponentString tempMutableComponent = parsePerMessageToComponent(messageSegment.getData());
            mutableComponent.appendSibling(tempMutableComponent);
            msgLogText.append(messageSegment.getData().getText());
        }
        logger.info(msgLogText.toString());
        return mutableComponent;
    }

    @Override
    public TextComponentString parseCommonBaseComponentListToComponent(List<CommonBaseComponent> myBaseComponentList) {

        TextComponentString mutableComponent = new TextComponentString("");
        for (CommonBaseComponent commonBaseComponent : myBaseComponentList) {
            TextComponentString tempMutableComponent = parsePerMessageToComponent(commonBaseComponent);
            mutableComponent.appendSibling(tempMutableComponent);
        }
        return mutableComponent;
    }


    @Override
    public TextComponentString parsePerMessageToComponent(CommonBaseComponent myBaseComponent) {

        TextComponentString tempMutableComponent = new TextComponentString(myBaseComponent.getText());

        Style style = getStyleFromBaseComponent(myBaseComponent);

        if (myBaseComponent instanceof CommonTextComponent) {
            CommonTextComponent commonTextComponent = (CommonTextComponent) myBaseComponent;
            if (commonTextComponent.getClickEvent() != null) {
                ClickEvent clickEventFromBaseComponent = getClickEventFromBaseComponent(commonTextComponent);
                if (clickEventFromBaseComponent != null)
                    style = style.setClickEvent(clickEventFromBaseComponent);
            }
            if (commonTextComponent.getHoverEvent() != null) {
                HoverEvent hoverEventFromBaseComponent = getHoverEventFromBaseComponent(commonTextComponent);
                if (hoverEventFromBaseComponent != null)
                    style = style.setHoverEvent(hoverEventFromBaseComponent);
            }
        }


        tempMutableComponent.setStyle(style);
        return tempMutableComponent;
    }


    private Style getStyleFromBaseComponent(CommonBaseComponent myBaseComponent) {
        ResourceLocation font = null;
        if (myBaseComponent.getFont() != null) {
            font = new ResourceLocation(myBaseComponent.getFont());
        }
        Style style = new Style()
                .setBold(myBaseComponent.isBold())
                .setItalic(myBaseComponent.isItalic())
                .setInsertion(myBaseComponent.getInsertion())
//                .setFont(font)
                .setUnderlined(myBaseComponent.isUnderlined())
                .setStrikethrough(myBaseComponent.isStrikethrough())
                .setObfuscated(myBaseComponent.isObfuscated());
        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
            style.setColor(TextFormatting.valueOf(myBaseComponent.getColor().toUpperCase()));
        else style.setColor(TextFormatting.WHITE);
        return style;
    }

    private ClickEvent getClickEventFromBaseComponent(CommonTextComponent myTextComponent) {
        if (myTextComponent.getClickEvent() != null) {
            ClickEvent.Action tempAction = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction().toLowerCase());
            return new ClickEvent(tempAction, myTextComponent.getClickEvent().getValue());
        }
        return null;
    }

    public HoverEvent getHoverEventFromBaseComponent(CommonTextComponent myTextComponent) {
        HoverEvent hoverEvent = null;
        HoverEvent.Action action = HoverEvent.Action.valueOf(myTextComponent.getHoverEvent().getAction().toLowerCase());
        if (action.equals(HoverEvent.Action.SHOW_TEXT)) {
            if (myTextComponent.getHoverEvent().getText() != null && !myTextComponent.getHoverEvent().getText().isEmpty()) {
                TextComponentString textComponent = parseCommonBaseComponentListToComponent(myTextComponent.getHoverEvent().getText());
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
            }
        } else if (action.equals(HoverEvent.Action.SHOW_ITEM)) {
            CommonHoverItem commonHoverItem = myTextComponent.getHoverEvent().getItem();
            Item item = Item.getItemById(Integer.parseInt(commonHoverItem.getId()));
            ItemStack itemStack = new ItemStack(item, commonHoverItem.getCount());
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(itemStack.getDisplayName()));
        } else if (action.equals(HoverEvent.Action.SHOW_ENTITY)) {
            CommonHoverEntity commonHoverEntity = myTextComponent.getHoverEvent().getEntity();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
        }
        return hoverEvent;
    }
}