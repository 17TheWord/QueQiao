package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import com.github.theword.queqiao.tool.payload.modle.CommonHoverEntity;
import com.github.theword.queqiao.tool.payload.modle.CommonHoverItem;
import com.github.theword.queqiao.tool.payload.modle.CommonTextComponent;
// IF >= forge-1.21
//import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
// ELSE IF >= forge-1.19
//import net.minecraft.network.chat.contents.LiteralContents;
// ELSE >= forge-1.18
// END IF

// IF > forge-1.16.5
//import net.minecraft.network.chat.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import java.util.Optional;
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

public class ParseJsonToEvent {

    // IF > forge-1.16.5
//    public MutableComponent parseMessages(List<? extends CommonBaseComponent> myBaseComponentList) {
    // ELSE
//    public StringTextComponent parseMessages(List<? extends CommonBaseComponent> myBaseComponentList) {
        // END IF

        // IF >= forge-1.19
//MutableComponent mutableComponent = MutableComponent.create(new LiteralContents(""));
        // ELSE IF >= forge-1.18 && < forge-1.19
//        MutableComponent mutableComponent = new TextComponent("");
        // ELSE
//        StringTextComponent mutableComponent = new StringTextComponent("");
        // END IF
        for (CommonBaseComponent myBaseComponent : myBaseComponentList) {
            // IF > forge-1.16.5
//            MutableComponent tempMutableComponent = parsePerMessageToMultiText(myBaseComponent);
            // ELSE
//            StringTextComponent tempMutableComponent = parsePerMessageToMultiText(myBaseComponent);
            // END IF
            mutableComponent.append(tempMutableComponent);
        }
        return mutableComponent;
    }




    // IF > forge-1.16.5
//    public MutableComponent parsePerMessageToMultiText(CommonBaseComponent myBaseComponent) {
    // ELSE
//    public StringTextComponent parsePerMessageToMultiText(CommonBaseComponent myBaseComponent) {
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
            CommonTextComponent myTextComponent = (CommonTextComponent) myBaseComponent;
            style = style.withClickEvent(getClickEventFromBaseComponent(myTextComponent));
            style = style.withHoverEvent(getHoverEventFromBaseComponent(myTextComponent));
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
                .withUnderlined(myBaseComponent.isUnderlined())
                .withInsertion(myBaseComponent.getInsertion())
                .withFont(font)
                // IF > forge-1.16.5
//                .withStrikethrough(myBaseComponent.isStrikethrough())
//                .withObfuscated(myBaseComponent.isObfuscated())
                // END IF
                ;
        // IF >= forge-1.21
//        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
//            style = style.withColor(TextColor.parseColor(myBaseComponent.getColor()).getOrThrow());
//        else style = style.withColor(TextColor.parseColor("white").getOrThrow());
//
        // ELSE IF > forge-1.16.5
//        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
//            style = style.withColor(TextColor.parseColor(myBaseComponent.getColor()));
//        else style = style.withColor(TextColor.parseColor("white"));
        // ELSE
//        if (myBaseComponent.getColor() != null && !myBaseComponent.getColor().isEmpty())
//            style.withColor(Color.parseColor(myBaseComponent.getColor()));
//        else style.withColor(Color.fromLegacyFormat(TextFormatting.WHITE));
        // END IF
        return style;
    }

    private ClickEvent getClickEventFromBaseComponent(CommonTextComponent myTextComponent) {
        if (myTextComponent.getClickEvent() != null) {
            // IF >= forge-1.21
//            ClickEvent.Action tempAction = ClickEvent.Action.valueOf(myTextComponent.getClickEvent().getAction());
            // ELSE
//            ClickEvent.Action tempAction = ClickEvent.Action.getByName(myTextComponent.getClickEvent().getAction());
            // END IF
            return new ClickEvent(tempAction, myTextComponent.getClickEvent().getValue());
        }
        return null;
    }

    public HoverEvent getHoverEventFromBaseComponent(CommonTextComponent myTextComponent) {
        if (myTextComponent.getHoverEvent() != null) {
            HoverEvent hoverEvent = null;
            switch (myTextComponent.getHoverEvent().getAction()) {
                case "show_text":
                    if (myTextComponent.getHoverEvent().getBaseComponentList() != null && !myTextComponent.getHoverEvent().getBaseComponentList().isEmpty()) {
                        // IF > forge-1.16.5
//                            MutableComponent textComponent = parseMessages(myTextComponent.getHoverEvent().getBaseComponentList());
                        // ELSE
//                        StringTextComponent textComponent = parseMessages(myTextComponent.getHoverEvent().getBaseComponentList());
                        // END IF
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
                    }
                    break;
                case "show_item":
                    CommonHoverItem myHoverItem = myTextComponent.getHoverEvent().getItem();
                    Item item = Item.byId(myHoverItem.getId());
                    ItemStack itemStack = new ItemStack(item, myHoverItem.getCount());
                    // IF > forge-1.16.5
//                    HoverEvent.ItemStackInfo itemHover = new HoverEvent.ItemStackInfo(itemStack);
                    // ELSE
//                    HoverEvent.ItemHover itemHover = new HoverEvent.ItemHover(itemStack);
//                    END IF
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemHover);
                    break;
                case "show_entity":
                    CommonHoverEntity myHoverEntity = myTextComponent.getHoverEvent().getEntity();
                    Optional<EntityType<?>> entityType = EntityType.byString(myHoverEntity.getType());
                    if (entityType.isPresent()) {
                        // IF > forge-1.16.5
//                        HoverEvent.EntityTooltipInfo entityTooltipInfo = new HoverEvent.EntityTooltipInfo(entityType.get(), UUID.randomUUID(), parseMessages(myHoverEntity.getName()));
                        // ELSE
//                        HoverEvent.EntityHover entityTooltipInfo = new HoverEvent.EntityHover(entityType.get(), UUID.randomUUID(), parseMessages(myHoverEntity.getName()));
                        // END IF
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
                    }
                    break;
                default:
                    break;
            }
            return hoverEvent;
        }
        return null;
    }
}