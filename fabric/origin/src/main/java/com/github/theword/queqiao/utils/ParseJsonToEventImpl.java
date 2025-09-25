package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
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

// IF >= fabric-1.21.5
//import java.net.URI;
//import java.net.URISyntaxException;
// END IF
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
        GlobalContext.getLogger().info(msgLogText.toString());
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
        // IF >= fabric-1.20.4
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
            // IF >= fabric-1.21
//           style = style.withColor(TextColor.parse(commonBaseComponent.getColor()).getOrThrow());
            // ELSE IF >= fabric-1.20.4
//           style = style.withColor(TextColor.parse(commonBaseComponent.getColor()).get().orThrow());
            // ELSE
//            style = style.withColor(TextColor.parse(commonBaseComponent.getColor()));
            // END IF
        } else style = style.withColor(TextColor.fromFormatting(Formatting.WHITE));

        if (commonBaseComponent instanceof CommonTextComponent) {
            CommonTextComponent commonTextComponent = (CommonTextComponent) commonBaseComponent;
            if (commonTextComponent.getClickEvent() != null)
                style = style.withClickEvent(getClickEvent(commonTextComponent));

            if (commonTextComponent.getHoverEvent() != null)
                style = style.withHoverEvent(getHoverEvent(commonTextComponent));
        }


        // IF < fabric-1.20.4 && >= fabric-1.19
//        MutableText mutableText = MutableText.of(tempTextContent);
//        mutableText.setStyle(style);
//        return mutableText;
        // ELSE
//        tempTextContent.setStyle(style);
//        return tempTextContent;
        // END IF
    }

    public ClickEvent getClickEvent(CommonTextComponent commonTextComponent) {
        ClickEvent clickEvent = null;
        // IF >= fabric-1.21.5
//        switch (commonTextComponent.getClickEvent().getAction().toLowerCase()) {
//            case "open_url":
//                try {
//                    clickEvent = new ClickEvent.OpenUrl(new URI(commonTextComponent.getClickEvent().getValue()));
//                } catch (URISyntaxException e) {
//                    GlobalContext.getLogger().warn("Invalid URL: {}", commonTextComponent.getClickEvent().getValue());
//                }
//                break;
//            case "open_file":
//                clickEvent = new ClickEvent.OpenFile(commonTextComponent.getClickEvent().getValue());
//                break;
//            case "run_command":
//                clickEvent = new ClickEvent.RunCommand(commonTextComponent.getClickEvent().getValue());
//                break;
//            case "suggest_command":
//                clickEvent = new ClickEvent.SuggestCommand(commonTextComponent.getClickEvent().getValue());
//                break;
//            case "change_page":
//                clickEvent = new ClickEvent.ChangePage(Integer.parseInt(commonTextComponent.getClickEvent().getValue()));
//                break;
//            default:
//                break;
//        }
        // ELSE IF >= fabric-1.20.4
//                ClickEvent.Action tempAction = ClickEvent.Action.valueOf(commonTextComponent.getClickEvent().getAction());
//                clickEvent = new ClickEvent(tempAction, commonTextComponent.getClickEvent().getValue());
        // ELSE
//        ClickEvent.Action tempAction = ClickEvent.Action.byName(commonTextComponent.getClickEvent().getAction());
//        clickEvent = new ClickEvent(tempAction, commonTextComponent.getClickEvent().getValue());
        // END IF
        return clickEvent;
    }

    public HoverEvent getHoverEvent(CommonTextComponent commonTextComponent) {
        HoverEvent hoverEvent = null;
        switch (commonTextComponent.getHoverEvent().getAction().toLowerCase()) {
            case "show_text":
                if (commonTextComponent.getHoverEvent().getText() != null && !commonTextComponent.getHoverEvent().getText().isEmpty()) {
                    Text textComponent = parseCommonBaseComponentListToComponent(commonTextComponent.getHoverEvent().getText());
                    // IF >= fabric-1.21.5
//                    hoverEvent = new HoverEvent.ShowText(textComponent);
                    // ELSE
//                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
                    // END IF
                }
                break;
            case "show_item":
                CommonHoverItem commonHoverItem = commonTextComponent.getHoverEvent().getItem();
                Item item = Item.byRawId(Integer.parseInt(commonHoverItem.getId()));
                ItemStack itemStack = new ItemStack(item, commonHoverItem.getCount());
                // IF >= fabric-1.21.5
//                hoverEvent = new HoverEvent.ShowItem(itemStack);
                // ELSE
//                HoverEvent.ItemStackContent itemStackContent = new HoverEvent.ItemStackContent(itemStack);
//                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemStackContent);
                // END IF
                break;
            case "show_entity":
                CommonHoverEntity commonHoverEntity = commonTextComponent.getHoverEvent().getEntity();
                Optional<EntityType<?>> entityType = EntityType.get(commonHoverEntity.getType());
                if (entityType.isPresent()) {
                    HoverEvent.EntityContent entityTooltipInfo = new HoverEvent.EntityContent(entityType.get(), UUID.randomUUID(), parseCommonBaseComponentListToComponent(commonHoverEntity.getName()));
                    // IF >= fabric-1.21.5
//                    hoverEvent = new HoverEvent.ShowEntity(entityTooltipInfo);
                    // ELSE
//                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
                    // END IF
                }
                break;
            default:
                break;
        }
        return hoverEvent;
    }
}