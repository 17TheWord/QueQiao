package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel;
import com.google.gson.JsonElement;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;


public class PaperTool {

    /**
     * 获取PaperPlayer
     *
     * @param player 玩家
     * @return PlayerModel
     */
    public static PlayerModel getPaperPlayer(Player player) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setNickname(player.getName());
        playerModel.setUuid(player.getUniqueId());
        playerModel.setOp(player.isOp());
        playerModel.setHealth(player.getHealth());
//        playerModel.setMaxHealth(player.getMaxHealth()); // Deprecated
        playerModel.setExperienceLevel(player.getLevel());
        playerModel.setExperienceProgress((double) player.getExp());
        playerModel.setTotalExperience(player.getTotalExperience());

        playerModel.setWalkSpeed((double) player.getWalkSpeed());
        playerModel.setX(player.getLocation().getX());
        playerModel.setY(player.getLocation().getY());
        playerModel.setZ(player.getLocation().getZ());
        return playerModel;
    }


    public static AchievementModel getPaperAdvancement(String nickname, Advancement advancement) {
        AchievementModel achievementModel = new AchievementModel();
        achievementModel.setKey(advancement.getKey().toString());
        DisplayModel displayModel = new DisplayModel();

        AdvancementDisplay advancementDisplay = advancement.getDisplay();
        if (advancementDisplay == null) {
            achievementModel.setDisplay(displayModel);
            return achievementModel;
        }

        displayModel.setTitle(parseTranslateModel(advancementDisplay.title()));
        displayModel.setDescription(parseTranslateModel(advancementDisplay.description()));
        displayModel.setFrame(advancementDisplay.frame().name());
        achievementModel.setDisplay(displayModel);

        TranslatableComponent translatable = Component.translatable(
                achievementModel.getTranslationKey(displayModel.getFrame()),
                Component.text(nickname),
                advancement.getDisplay().title()
        );

        achievementModel.setTranslation(parseTranslateModel(translatable));
        return achievementModel;
    }

    public static Component buildComponent(JsonElement jsonElement) {
        return GsonComponentSerializer.gson().deserializeFromTree(jsonElement);
    }

    public static String getComponentText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getComponentJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static TranslateModel parseTranslateModel(Component component) {
        // 1. 提前处理非翻译组件
        if (!(component instanceof TranslatableComponent translatable)) {
            return new TranslateModel(null, null, getComponentText(component));
        }

        // 2. 提取 Key 和 Args
        String key = translatable.key();
        // 1.17.1 中返回的是 List<Component>
        List<Component> args = translatable.args();

        TranslateModel[] childModels = new TranslateModel[args.size()];
        String[] stringsForFormat = new String[args.size()];

        for (int i = 0; i < args.size(); i++) {
            Component argComponent = args.get(i);

            // 3. 递归处理参数
            // 因为已经是 Component 类型，直接递归即可，无需 instanceof 检查和解包
            TranslateModel child = parseTranslateModel(argComponent);

            childModels[i] = child;
            stringsForFormat[i] = child.getText();
        }

        // 4. 格式化逻辑
        String finalText = GlobalContext.translate(key, stringsForFormat);

        // 5. 回声检查与子组件拼接
        if (finalText.equals(key)) {
            finalText = getComponentText(component);
        } else if (!component.children().isEmpty()) {
            // 拼接 Siblings (Adventure 中称为 Children)
            StringBuilder sb = new StringBuilder(finalText);
            for (Component childComponent : component.children()) {
                sb.append(parseTranslateModel(childComponent).getText());
            }
            finalText = sb.toString();
        }

        return new TranslateModel(key, childModels, finalText);
    }
}