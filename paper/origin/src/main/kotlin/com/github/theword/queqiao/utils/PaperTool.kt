package com.github.theword.queqiao.utils

import com.github.theword.queqiao.tool.GlobalContext
import com.github.theword.queqiao.tool.event.model.PlayerModel
import com.github.theword.queqiao.tool.event.model.TranslateModel
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel
import com.github.theword.queqiao.tool.event.model.achievement.DisplayModel
import com.google.gson.JsonElement
import io.papermc.paper.advancement.AdvancementDisplay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player


class PaperTool {

    companion object {
        /**
         * 获取PaperPlayer
         *
         * @param player 玩家
         * @return PlayerModel
         */
        fun getPaperPlayer(player: Player): PlayerModel {
            val playerModel = PlayerModel()
            playerModel.nickname = player.name
            playerModel.uuid = player.uniqueId
            playerModel.op = player.isOp
            playerModel.health = player.health
            //        playerModel.setMaxHealth(player.getMaxHealth()); // Deprecated
            playerModel.experienceLevel = player.level
            playerModel.experienceProgress = player.exp.toDouble()
            playerModel.totalExperience = player.totalExperience

            playerModel.walkSpeed = player.walkSpeed.toDouble()
            playerModel.x = player.location.x
            playerModel.y = player.location.y
            playerModel.z = player.location.z
            return playerModel
        }


        fun getPaperAdvancement(nickname: String?, advancement: Advancement): AchievementModel {
            val achievementModel = AchievementModel()
            achievementModel.key = advancement.key.toString()
            val displayModel = DisplayModel()

            val advancementDisplay: AdvancementDisplay? = advancement.display
            if (advancementDisplay == null) {
                achievementModel.display = displayModel
                return achievementModel
            }

            displayModel.title = parseTranslateModel(advancementDisplay.title())
            displayModel.description = parseTranslateModel(advancementDisplay.description())
            displayModel.frame = advancementDisplay.frame().name
            achievementModel.display = displayModel

            val translatable: TranslatableComponent? = nickname?.let {
                Component.translatable(
                    achievementModel.getTranslationKey(displayModel.frame),
                    Component.text(it),
                    advancement.display!!.title()
                )
            }

            achievementModel.translation = parseTranslateModel(translatable)
            return achievementModel
        }

        fun buildComponent(jsonElement: JsonElement): Component {
            return GsonComponentSerializer.gson().deserializeFromTree(jsonElement)
        }

        fun getComponentText(component: Component): String {
            return PlainTextComponentSerializer.plainText().serialize(component)
        }

        fun getComponentJson(component: Component): String {
            return GsonComponentSerializer.gson().serialize(component)
        }

        fun parseTranslateModel(component: Component?): TranslateModel {
            // 1. 提前处理非翻译组件
            if (component !is TranslatableComponent) {
                return TranslateModel(null, null, getComponentText(component!!))
            }

            // 2. 提取 Key 和 Args
            val key = component.key()
            // 返回的是 List<Component>
            val args = component.arguments()

            val childModels = arrayOfNulls<TranslateModel>(args.size)
            val stringsForFormat = arrayOfNulls<String>(args.size)

            for (i in args.indices) {
                val argComponent = args[i]

                // 3. 递归处理参数
                // 因为已经是 Component 类型，直接递归即可，无需 instanceof 检查和解包
                val child = parseTranslateModel(argComponent.asComponent())

                childModels[i] = child
                stringsForFormat[i] = child.text
            }

            // 4. 格式化逻辑
            var finalText = GlobalContext.translate(key, stringsForFormat)

            // 5. 回声检查与子组件拼接
            if (finalText == key) {
                finalText = getComponentText(component)
            } else if (!component.children().isEmpty()) {
                // 拼接 Siblings (Adventure 中称为 Children)
                val sb = StringBuilder(finalText)
                for (childComponent in component.children()) {
                    sb.append(parseTranslateModel(childComponent).text)
                }
                finalText = sb.toString()
            }

            return TranslateModel(key, childModels, finalText)
        }
    }
}