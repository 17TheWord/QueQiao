package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;
// IF >= forge-1.21
//import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
// ELSE IF >= forge-1.19
//import net.minecraft.network.chat.contents.LiteralContents;
// ELSE >= forge-1.18
// END IF

// IF > forge-1.16.5
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
// ELSE
//import net.minecraft.entity.EntityType;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.text.Color;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.item.Item;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.Style;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.util.text.event.ClickEvent;
//import net.minecraft.util.text.event.HoverEvent;
// END IF
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.theword.queqiao.tool.utils.Tool.logger;

public class ParseJsonToEventImpl implements ParseJsonToEventService {

    @Override
    // IF > forge-1.16.5
//    public MutableComponent parseMessageListToComponent(List<MessageSegment> myBaseComponentList) {
        // ELSE
//    public StringTextComponent parseMessageListToComponent(List<MessageSegment> myBaseComponentList) {
        // END IF

        // IF >= forge-1.19
//        MutableComponent mutableComponent = MutableComponent.create(new LiteralContents(""));
        // ELSE IF >= forge-1.18 && < forge-1.19
//        MutableComponent mutableComponent = new TextComponent("");
        // ELSE
//        StringTextComponent mutableComponent = new StringTextComponent("");
        // END IF
        StringBuilder msgLogText = new StringBuilder();
        for (MessageSegment messageSegment : myBaseComponentList) {
            // IF > forge-1.16.5
//            MutableComponent tempMutableComponent = parsePerMessageToComponent(messageSegment.getData());
            // ELSE
//            StringTextComponent tempMutableComponent = parsePerMessageToComponent(messageSegment.getData());
            // END IF
            mutableComponent.append(tempMutableComponent);
            msgLogText.append(messageSegment.getData().getText());
        }
        logger.info(msgLogText.toString());
        return mutableComponent;
    }

    @Override
    // IF > forge-1.16.5
//    public MutableComponent parseCommonBaseComponentListToComponent(List<CommonBaseComponent> myBaseComponentList) {
        // ELSE
//    public StringTextComponent parseCommonBaseComponentListToComponent(List<CommonBaseComponent> myBaseComponentList) {
        // END IF

        // IF >= forge-1.19
//        MutableComponent mutableComponent = MutableComponent.create(new LiteralContents(""));
        // ELSE IF >= forge-1.18 && < forge-1.19
//        MutableComponent mutableComponent = new TextComponent("");
        // ELSE
//        StringTextComponent mutableComponent = new StringTextComponent("");
        // END IF
        for (CommonBaseComponent commonBaseComponent : myBaseComponentList) {
            // IF > forge-1.16.5
//            MutableComponent tempMutableComponent = parsePerMessageToComponent(commonBaseComponent);
            // ELSE
//            StringTextComponent tempMutableComponent = parsePerMessageToComponent(commonBaseComponent);
            // END IF
            mutableComponent.append(tempMutableComponent);
        }
        return mutableComponent;
    }


    @Override
    // IF > forge-1.16.5
//    public MutableComponent parsePerMessageToComponent(CommonBaseComponent myBaseComponent) {
// ELSE
//    public StringTextComponent parsePerMessageToComponent(CommonBaseComponent myBaseComponent) {
// END IF

// IF >= forge-1.19
//        LiteralContents tempMutableComponent = new LiteralContents(myBaseComponent.getText());
// ELSE IF >= forge-1.18 && < forge-1.19
//        TextComponent tempMutableComponent = new TextComponent(myBaseComponent.getText());
// ELSE
//        StringTextComponent tempMutableComponent = new StringTextComponent(myBaseComponent.getText());
// END IF

        Style style = getStyleFromBaseComponent(myBaseComponent);

        if (myBaseComponent instanceof CommonTextComponent) {
            CommonTextComponent commonTextComponent = (CommonTextComponent) myBaseComponent;
            if (commonTextComponent.getClickEvent() != null)
                style = style.withClickEvent(getClickEventFromBaseComponent(commonTextComponent));
            if (commonTextComponent.getHoverEvent() != null)
                style = style.withHoverEvent(getHoverEventFromBaseComponent(commonTextComponent));
        }


        // IF >= forge-1.19
//        MutableComponent mutableComponent = MutableComponent.create(tempMutableComponent);
//        mutableComponent.setStyle(style);
//        return mutableComponent;
        // ELSE
//        tempMutableComponent.setStyle(style);
//        return tempMutableComponent;
        // END IF
    }


    private Style getStyleFromBaseComponent(CommonBaseComponent myBaseComponent) {
        ResourceLocation font = null;
        if (myBaseComponent.getFont() != null) {
            // IF >= forge-1.21
//            font = ResourceLocation.parse(myBaseComponent.getFont());
            // ELSE
//            font = new ResourceLocation(myBaseComponent.getFont());
            // END IF
        }
        Style style = Style.EMPTY
                .withBold(myBaseComponent.isBold())
                .withItalic(myBaseComponent.isItalic())
                .withInsertion(myBaseComponent.getInsertion())
                .withFont(font)
                // IF > forge-1.16.5
//                .withUnderlined(myBaseComponent.isUnderlined())
//                .withStrikethrough(myBaseComponent.isStrikethrough())
//                .withObfuscated(myBaseComponent.isObfuscated())
                // ELSE
//                .setUnderlined(myBaseComponent.isUnderlined())
//                .setStrikethrough(myBaseComponent.isStrikethrough())
//                .setObfuscated(myBaseComponent.isObfuscated())
                // END IF
                ;
        // IF >= forge-1.21
//        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
//            style = style.withColor(TextColor.parseColor(myBaseComponent.getColor()).getOrThrow());
//        else style = style.withColor(TextColor.fromLegacyFormat(ChatFormatting.WHITE));
        // ELSE IF > forge-1.16.5
//        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
//            style = style.withColor(TextColor.parseColor(myBaseComponent.getColor()));
//        else style = style.withColor(TextColor.fromLegacyFormat(ChatFormatting.WHITE));
        // ELSE
//        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
//            style.withColor(Color.fromLegacyFormat(TextFormatting.valueOf(myBaseComponent.getColor().toUpperCase())));
//        else style.withColor(Color.fromLegacyFormat(TextFormatting.WHITE));
        // END IF
        return style;
    }

    private ClickEvent getClickEventFromBaseComponent(CommonTextComponent myTextComponent) {
        if (myTextComponent.getClickEvent() != null) {
            // IF >= forge-1.21
//            ClickEvent.Action tempAction = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction().toLowerCase());
            // ELSE
//            ClickEvent.Action tempAction = ClickEvent.Action.getByName(myTextComponent.getClickEvent().getAction().toLowerCase());
            // END IF
            return new ClickEvent(tempAction, myTextComponent.getClickEvent().getValue());
        }
        return null;
    }

    public HoverEvent getHoverEventFromBaseComponent(CommonTextComponent myTextComponent) {
        HoverEvent hoverEvent = null;
        // IF >= forge-1.21
//        HoverEvent.Action<?> action = HoverEvent.Action.SHOW_TEXT;
        // ELSE
//        HoverEvent.Action<?> action = HoverEvent.Action.getByName(myTextComponent.getHoverEvent().getAction().toLowerCase());
        // END IF
        assert action != null;
        if (action.equals(HoverEvent.Action.SHOW_TEXT)) {
            if (myTextComponent.getHoverEvent().getText() != null && !myTextComponent.getHoverEvent().getText().isEmpty()) {
                // IF > forge-1.16.5
//                MutableComponent textComponent = parseCommonBaseComponentListToComponent(myTextComponent.getHoverEvent().getText());
                // ELSE
//                        StringTextComponent textComponent = parseCommonBaseComponentListToComponent(myTextComponent.getHoverEvent().getText());
                // END IF
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
            }
        } else if (action.equals(HoverEvent.Action.SHOW_ITEM)) {
            CommonHoverItem commonHoverItem = myTextComponent.getHoverEvent().getItem();
            Item item = Item.byId(Integer.parseInt(commonHoverItem.getId()));
            ItemStack itemStack = new ItemStack(item, commonHoverItem.getCount());
            // IF > forge-1.16.5
//            HoverEvent.ItemStackInfo itemHover = new HoverEvent.ItemStackInfo(itemStack);
            // ELSE
//                    HoverEvent.ItemHover itemHover = new HoverEvent.ItemHover(itemStack);
//                    END IF
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemHover);
        } else if (action.equals(HoverEvent.Action.SHOW_ENTITY)) {
            CommonHoverEntity commonHoverEntity = myTextComponent.getHoverEvent().getEntity();
            Optional<EntityType<?>> entityType = EntityType.byString(commonHoverEntity.getType());
            if (entityType.isPresent()) {
                // IF > forge-1.16.5
//                HoverEvent.EntityTooltipInfo entityTooltipInfo = new HoverEvent.EntityTooltipInfo(entityType.get(), UUID.randomUUID(), parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
                // ELSE
//                        HoverEvent.EntityHover entityTooltipInfo = new HoverEvent.EntityHover(entityType.get(), UUID.randomUUID(), parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
                // END IF
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
            }
        }
        return hoverEvent;
    }
}