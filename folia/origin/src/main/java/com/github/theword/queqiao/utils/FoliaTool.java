package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;


public class FoliaTool {

    public static String getComponentText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getComponentJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }


    /**
     * 获取SpigotPlayer
     *
     * @param foliaPlayer 玩家
     * @return FoliaPlayer
     */
    public static PlayerModel getFoliaPlayer(Player foliaPlayer) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(foliaPlayer.getName());
        playerModel.setUuid(foliaPlayer.getUniqueId());

        if (foliaPlayer.getAddress() != null)
            playerModel.setAddress(foliaPlayer.getAddress().getHostString());

        playerModel.setHealth(foliaPlayer.getHealth());
//        playerModel.setMaxHealth(foliaPlayer.getMaxHealth()); // Deprecated
        playerModel.setExperienceLevel(foliaPlayer.getLevel());
        playerModel.setExperienceProgress((double) foliaPlayer.getExp());
        playerModel.setTotalExperience(foliaPlayer.getTotalExperience());

        playerModel.setOp(foliaPlayer.isOp());
        playerModel.setWalkSpeed((double) foliaPlayer.getWalkSpeed());
        playerModel.setX(foliaPlayer.getLocation().getX());
        playerModel.setY(foliaPlayer.getLocation().getY());
        playerModel.setZ(foliaPlayer.getLocation().getZ());

        return playerModel;
    }

    public static TranslateModel parseTranslateModel(Component component) {
        // 1. 提前处理非翻译组件（Guard Clause）
        if (!(component instanceof TranslatableComponent translatable)) {
            return new TranslateModel(null, null, getComponentText(component));
        }

        // 2. 只有翻译组件才会执行到这里
        String key = translatable.key();
        List<TranslationArgument> arguments = translatable.arguments();

        TranslateModel[] childModels = new TranslateModel[arguments.size()];
        String[] stringsForFormat = new String[arguments.size()];

        for (int i = 0; i < arguments.size(); i++) {
            TranslationArgument arg = arguments.get(i);
            // 注意：必须调用 .value() 才能拿到内部包裹的 Component 或基础类型
            Object val = arg.value();

            TranslateModel child;
            if (val instanceof Component childComponent) {
                // 递归解析，保留深层 key/args
                child = parseTranslateModel(childComponent);
            } else {
                // 处理 Boolean, Number 等基础类型参数
                child = new TranslateModel(null, null, String.valueOf(val));
            }

            childModels[i] = child;
            stringsForFormat[i] = child.getText();
        }

        // 3. 业务逻辑：尝试翻译并回填
        String finalText = GlobalContext.translate(key, stringsForFormat);

        // 4. 回声检查：如果找不到 Key，由你已有的 getComponentText 兜底
        if (finalText.equals(key)) {
            finalText = getComponentText(component);
        } else if (!component.children().isEmpty()) {
            // 补充：如果翻译成功，但组件带有子组件（Siblings），需要拼接
            StringBuilder sb = new StringBuilder(finalText);
            for (Component childComponent : component.children()) {
                sb.append(parseTranslateModel(childComponent).getText());
            }
            finalText = sb.toString();
        }

        return new TranslateModel(key, childModels, finalText);
    }

}