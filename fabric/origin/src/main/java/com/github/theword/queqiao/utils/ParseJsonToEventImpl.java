package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.handle.ParseJsonToEventService;
import com.github.theword.queqiao.tool.payload.MessageSegment;
import com.github.theword.queqiao.tool.payload.modle.component.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.component.CommonTextComponent;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.hover.CommonHoverItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.theword.queqiao.tool.utils.Tool.logger;

public class ParseJsonToEventImpl implements ParseJsonToEventService {

    @Override
    public Text parseMessageListToComponent(List<MessageSegment> myBaseComponentList) {
        // IF > fabric-1.18.2
//        MutableText mutableText = Text.empty();
        // ELSE
//        MutableText mutableText = LiteralText.EMPTY.copy();
        // END IF
        StringBuilder msgLogText = new StringBuilder();
        for (MessageSegment messageSegment : myBaseComponentList) {
            mutableText.append(parsePerMessageToComponent(messageSegment.getData()));
            msgLogText.append(messageSegment.getData().getText());
        }
        logger.info(msgLogText.toString());
        return mutableText;
    }

    @Override
    public Text parseCommonBaseComponentListToComponent(List<CommonBaseComponent> list) {
        // IF > fabric-1.18.2
//        MutableText mutableText = Text.empty();
        // ELSE
//        MutableText mutableText = LiteralText.EMPTY.copy();
        // END IF
        for (CommonBaseComponent commonBaseComponent : list) {
            mutableText.append(parsePerMessageToComponent(commonBaseComponent));
        }
        return mutableText;
    }

    @Override
    public MutableText parsePerMessageToComponent(CommonBaseComponent commonBaseComponent) {
        // IF >= fabric-1.21
//        MutableText tempTextContent = Text.literal(commonBaseComponent.getText());
        // ELSE IF >= fabric-1.19
//        LiteralTextContent tempTextContent = new LiteralTextContent(commonBaseComponent.getText());
        // ELSE
//        LiteralText tempTextContent = new LiteralText(commonBaseComponent.getText());
        // END IF
        Identifier identifier = null;
        if (commonBaseComponent.getFont() != null) {
            // IF >= fabric-1.21
//            identifier = Identifier.of(Identifier.DEFAULT_NAMESPACE, commonBaseComponent.getFont());
            // ELSE IF >= fabric-1.18
//            identifier = new Identifier(Identifier.DEFAULT_NAMESPACE, commonBaseComponent.getFont());
            // ELSE
//            identifier = new Identifier(commonBaseComponent.getFont());
            // END IF
        }

        Style style = Style.EMPTY
                .withBold(commonBaseComponent.isBold())
                .withItalic(commonBaseComponent.isItalic())
                .withUnderline(commonBaseComponent.isUnderlined())
                .withInsertion(commonBaseComponent.getInsertion())
                .withFont(identifier)
                // IF > fabric-1.16.5
//                .withObfuscated(commonBaseComponent.isObfuscated())
//                .withStrikethrough(commonBaseComponent.isStrikethrough())
                // END IF
                ;

        if (commonBaseComponent.getColor() != null && !commonBaseComponent.getColor().isEmpty()) {
            // IF fabric-1.21
//            style.withColor(TextColor.parse(commonBaseComponent.getColor()).getOrThrow());
            // ELSE
//            style.withColor(TextColor.parse(commonBaseComponent.getColor()));
            // END IF
        } else style.withColor(TextColor.fromFormatting(Formatting.WHITE));

        if (commonBaseComponent instanceof CommonTextComponent) {
            CommonTextComponent commonTextComponent = (CommonTextComponent) commonBaseComponent;
            if (commonTextComponent.getClickEvent() != null)
                style.withClickEvent(getClickEvent(commonTextComponent));

            if (commonTextComponent.getHoverEvent() != null)
                style.withHoverEvent(getHoverEvent(commonTextComponent));
        }


        // IF < fabric-1.21 && >= fabric-1.19
//        MutableText mutableText = MutableText.of(tempTextContent);
//        mutableText.setStyle(style);
//        return mutableText;
        // ELSE
//        tempTextContent.setStyle(style);
//        return tempTextContent;
        // END IF
    }

    public ClickEvent getClickEvent(CommonTextComponent commonTextComponent) {
        // IF fabric-1.21
//                ClickEvent.Action tempAction = ClickEvent.Action.valueOf(commonTextComponent.getClickEvent().getAction());
        // ELSE
//        ClickEvent.Action tempAction = ClickEvent.Action.byName(commonTextComponent.getClickEvent().getAction());
        // END IF
        return new ClickEvent(tempAction, commonTextComponent.getClickEvent().getValue());
    }

    public HoverEvent getHoverEvent(CommonTextComponent commonTextComponent) {
        HoverEvent hoverEvent = null;
        switch (commonTextComponent.getHoverEvent().getAction().toLowerCase()) {
            case "show_text":
                if (commonTextComponent.getHoverEvent().getText() != null && !commonTextComponent.getHoverEvent().getText().isEmpty()) {
                    Text textComponent = parseCommonBaseComponentListToComponent(commonTextComponent.getHoverEvent().getText());
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
                }
                break;
            case "show_item":
                CommonHoverItem commonHoverItem = commonTextComponent.getHoverEvent().getItem();
                Item item = Item.byRawId(Integer.parseInt(commonHoverItem.getId()));
                ItemStack itemStack = new ItemStack(item, commonHoverItem.getCount());
                HoverEvent.ItemStackContent itemStackContent = new HoverEvent.ItemStackContent(itemStack);
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemStackContent);
                break;
            case "show_entity":
                CommonHoverEntity commonHoverEntity = commonTextComponent.getHoverEvent().getEntity();
                Optional<EntityType<?>> entityType = EntityType.get(commonHoverEntity.getType());
                if (entityType.isPresent()) {
                    HoverEvent.EntityContent entityTooltipInfo = new HoverEvent.EntityContent(entityType.get(), UUID.randomUUID(), parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
                }
                break;
            default:
                break;
        }
        return hoverEvent;
    }
}