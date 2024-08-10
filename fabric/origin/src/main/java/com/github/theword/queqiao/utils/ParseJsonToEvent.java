package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.CommonHoverItem;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ParseJsonToEvent {

    public Text parseMessages(List<? extends CommonBaseComponent> myBaseComponentList) {
        // IF > fabric-1.18.2
//        MutableText mutableText = Text.empty();
        // ELSE
//        MutableText mutableText = LiteralText.EMPTY.copy();
        // END IF
        for (CommonBaseComponent myBaseComponent : myBaseComponentList) {
            mutableText.append(parsePerMessageToMultiText(myBaseComponent));
        }
        return mutableText;
    }

    public MutableText parsePerMessageToMultiText(CommonBaseComponent myBaseComponent) {
        // IF >= fabric-1.21
//        MutableText tempTextContent = Text.literal(myBaseComponent.getText());
        // ELSE IF >= fabric-1.19
//        LiteralTextContent tempTextContent = new LiteralTextContent(myBaseComponent.getText());
        // ELSE
//        LiteralText tempTextContent = new LiteralText(myBaseComponent.getText());
        // END IF
        Identifier identifier = null;
        if (myBaseComponent.getFont() != null) {
            // IF >= fabric-1.21
//            identifier = Identifier.of(Identifier.DEFAULT_NAMESPACE, myBaseComponent.getFont());
            // ELSE IF >= fabric-1.18
//            identifier = new Identifier(Identifier.DEFAULT_NAMESPACE, myBaseComponent.getFont());
            // ELSE
//            identifier = new Identifier(myBaseComponent.getFont());
            // END IF
        }

        Style style = Style.EMPTY
                .withBold(myBaseComponent.isBold())
                .withItalic(myBaseComponent.isItalic())
                .withUnderline(myBaseComponent.isUnderlined())
                .withInsertion(myBaseComponent.getInsertion())
                .withFont(identifier);
                // IF > fabric-1.16.5
//                .withObfuscated(myBaseComponent.isObfuscated())
//                .withStrikethrough(myBaseComponent.isStrikethrough())
                // END IF

        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty()) {
            // IF fabric-1.21
//            style.withColor(TextColor.parse(myBaseComponent.getColor()).getOrThrow());
            // ELSE
//            style.withColor(TextColor.parse(myBaseComponent.getColor()));
            // END IF
        } else {
            // IF fabric-1.21
//            style.withColor(TextColor.fromFormatting(Formatting.WHITE));
            // ELSE
//            style.withColor(TextColor.fromFormatting(Formatting.WHITE));
            // END IF
        }

        // 配置 TextComponent 额外属性
        // IF > fabric-1.16.5
//        if (myBaseComponent instanceof CommonTextComponent myTextComponent) {
        // ELSE
//        if (myBaseComponent instanceof CommonTextComponent) {
//            CommonTextComponent myTextComponent = (CommonTextComponent) myBaseComponent;
            // END IF
            if (myTextComponent.getClickEvent() != null) {
                // IF fabric-1.21
//                ClickEvent.Action tempAction = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction());
                // ELSE
//                ClickEvent.Action tempAction = ClickEvent.Action.byName(myTextComponent.getClickEvent().getAction());
                // END IF
                ClickEvent clickEvent = new ClickEvent(tempAction, myTextComponent.getClickEvent().getValue());
                style.withClickEvent(clickEvent);
            }
            if (myTextComponent.getHoverEvent() != null) {
                HoverEvent hoverEvent = null;
                // 语言级别 '8' 不支持 增强的 'switch' 块
                switch (myTextComponent.getHoverEvent().getAction()) {
                    case "show_text":
                        if (myTextComponent.getHoverEvent().getBaseComponentList() != null && !myTextComponent.getHoverEvent().getBaseComponentList().isEmpty()) {
                            Text textComponent = parseMessages(myTextComponent.getHoverEvent().getBaseComponentList());
                            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
                        }
                        break;
                    case "show_item":
                        CommonHoverItem myHoverItem = myTextComponent.getHoverEvent().getItem();
                        Item item = Item.byRawId(myHoverItem.getId());
                        ItemStack itemStack = new ItemStack(item, myHoverItem.getCount());
                        HoverEvent.ItemStackContent itemStackContent = new HoverEvent.ItemStackContent(itemStack);
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemStackContent);
                        break;
                    case "show_entity":
                        CommonHoverEntity myHoverEntity = myTextComponent.getHoverEvent().getEntity();
                        Optional<EntityType<?>> entityType = EntityType.get(myHoverEntity.getType());
                        if (entityType.isPresent()) {
                            HoverEvent.EntityContent entityTooltipInfo = new HoverEvent.EntityContent(entityType.get(), UUID.randomUUID(), parseMessages(myHoverEntity.getName()));
                            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
                        }
                        break;
                    default:
                        break;
                }
                style.withHoverEvent(hoverEvent);
            }
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
}