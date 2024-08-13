package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.payload.modle.CommonBaseComponent;
import net.kyori.adventure.text.Component;


import java.util.List;

public class ParseJsonToEvent {

    public Component parseMessage(List<? extends CommonBaseComponent> commonBaseComponentList) {
        Component component = Component.empty();
        for (CommonBaseComponent commonBaseComponent : commonBaseComponentList) {
            component = component.append(Component.text(commonBaseComponent.getText()));
        }
        return component;
    }

    public Component parsePerMessageToMultiText(CommonBaseComponent commonBaseComponent) {
        Component component = Component.text(commonBaseComponent.getText());
        return component;
    }

}
