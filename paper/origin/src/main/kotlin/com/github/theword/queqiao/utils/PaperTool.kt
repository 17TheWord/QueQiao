package com.github.theword.queqiao.utils

import com.github.theword.queqiao.PlatformContext
import com.google.gson.JsonElement
import io.github.theword.queqiao.core.event.model.PlayerModel
import io.github.theword.queqiao.core.event.model.achievement.AchievementModel
import io.github.theword.queqiao.core.event.model.achievement.DisplayModel
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


        fun getPaperAdvancement(
            context: PlatformContext,
            nickname: String?,
            advancement: Advancement
        ): AchievementModel {
            val achievementModel = AchievementModel()
            achievementModel.key = advancement.key.toString()
            val displayModel = DisplayModel()

            val advancementDisplay: AdvancementDisplay? = advancement.display
            if (advancementDisplay == null) {
                achievementModel.display = displayModel
                return achievementModel
            }

            displayModel.title = context.parseTranslateModel(advancementDisplay.title())
            displayModel.description = context.parseTranslateModel(advancementDisplay.description())
            displayModel.frame = advancementDisplay.frame().name
            achievementModel.display = displayModel

            val translatable: TranslatableComponent? = nickname?.let {
                Component.translatable(
                    achievementModel.getTranslationKey(displayModel.frame),
                    Component.text(it),
                    advancement.display!!.title()
                )
            }

            if (translatable != null) {
                achievementModel.translation = context.parseTranslateModel(translatable)
            }
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
    }
}
