package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.theword.queqiao.tool.utils.Tool.logger;

public class ParseJsonToEventImpl implements ParseJsonToEventService {

    @Override
    public MutableComponent parseMessageListToComponent(List<MessageSegment> myBaseComponentList) {
        MutableComponent mutableComponent = MutableComponent.create(new LiteralContents(""));
        StringBuilder msgLogText = new StringBuilder();
        for (MessageSegment messageSegment : myBaseComponentList) {
            MutableComponent tempMutableComponent = parsePerMessageToComponent(messageSegment.getData());
            mutableComponent.append(tempMutableComponent);
            msgLogText.append(messageSegment.getData().getText());
        }
        logger.info(msgLogText.toString());
        return mutableComponent;
    }

    @Override
    public MutableComponent parseCommonBaseComponentListToComponent(List<CommonBaseComponent> myBaseComponentList) {
        MutableComponent mutableComponent = MutableComponent.create(new LiteralContents(""));
        for (CommonBaseComponent commonBaseComponent : myBaseComponentList) {
            MutableComponent tempMutableComponent = parsePerMessageToComponent(commonBaseComponent);
            mutableComponent.append(tempMutableComponent);
        }
        return mutableComponent;
    }


    @Override
    public MutableComponent parsePerMessageToComponent(CommonBaseComponent myBaseComponent) {
        LiteralContents tempMutableComponent = new LiteralContents(myBaseComponent.getText());

        Style style = getStyleFromBaseComponent(myBaseComponent);

        if (myBaseComponent instanceof CommonTextComponent) {
            CommonTextComponent commonTextComponent = (CommonTextComponent) myBaseComponent;
            if (commonTextComponent.getClickEvent() != null)
                style = style.withClickEvent(getClickEventFromBaseComponent(commonTextComponent));
            if (commonTextComponent.getHoverEvent() != null)
                style = style.withHoverEvent(getHoverEventFromBaseComponent(commonTextComponent));
        }


        MutableComponent mutableComponent = MutableComponent.create(tempMutableComponent);
        mutableComponent.setStyle(style);
        return mutableComponent;
    }


    private Style getStyleFromBaseComponent(CommonBaseComponent myBaseComponent) {
        ResourceLocation font = null;
        if (myBaseComponent.getFont() != null) {
            font = ResourceLocation.parse(myBaseComponent.getFont());
        }
        Style style = Style.EMPTY
                .withBold(myBaseComponent.isBold())
                .withItalic(myBaseComponent.isItalic())
                .withInsertion(myBaseComponent.getInsertion())
                .withFont(font)
                .withUnderlined(myBaseComponent.isUnderlined())
                .withStrikethrough(myBaseComponent.isStrikethrough())
                .withObfuscated(myBaseComponent.isObfuscated());
        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
            style = style.withColor(TextColor.parseColor(myBaseComponent.getColor()).getOrThrow());
        else style = style.withColor(TextColor.fromLegacyFormat(ChatFormatting.WHITE));
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
        HoverEvent.Action<?> action;
        switch (myTextComponent.getHoverEvent().getAction().toLowerCase()) {
            case "show_text":
                action = HoverEvent.Action.SHOW_TEXT;
                break;
            case "show_item":
                action = HoverEvent.Action.SHOW_ITEM;
                break;
            case "show_entity":
                action = HoverEvent.Action.SHOW_ENTITY;
                break;
            default:
                action = null;
                break;
        }

        assert action != null;
        if (action.equals(HoverEvent.Action.SHOW_TEXT)) {
            if (myTextComponent.getHoverEvent().getText() != null && !myTextComponent.getHoverEvent().getText().isEmpty()) {
                MutableComponent textComponent = parseCommonBaseComponentListToComponent(myTextComponent.getHoverEvent().getText());
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
            }
        } else if (action.equals(HoverEvent.Action.SHOW_ITEM)) {
            CommonHoverItem commonHoverItem = myTextComponent.getHoverEvent().getItem();
            Item item = Item.byId(Integer.parseInt(commonHoverItem.getId()));
            ItemStack itemStack = new ItemStack(item, commonHoverItem.getCount());
            HoverEvent.ItemStackInfo itemHover = new HoverEvent.ItemStackInfo(itemStack);
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemHover);
        } else {
            CommonHoverEntity commonHoverEntity = myTextComponent.getHoverEvent().getEntity();
            Optional<EntityType<?>> entityType = EntityType.byString(commonHoverEntity.getType());
            if (entityType.isPresent()) {
                HoverEvent.EntityTooltipInfo entityTooltipInfo = new HoverEvent.EntityTooltipInfo(entityType.get(), UUID.randomUUID(), parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
            }
        }
        return hoverEvent;
    }
}