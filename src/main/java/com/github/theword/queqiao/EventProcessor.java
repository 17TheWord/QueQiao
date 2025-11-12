package com.github.theword.queqiao;

import static com.github.theword.queqiao.tool.utils.Tool.isRegisterOrLoginCommand;
import static com.github.theword.queqiao.utils.ForgeTool.getForgeAchievement;
import static com.github.theword.queqiao.utils.ForgeTool.getPlayerModel;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.*;
import com.github.theword.queqiao.tool.event.model.PlayerModel;
import com.github.theword.queqiao.tool.event.model.achievement.AchievementModel;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;


import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

import java.util.Arrays;


public class EventProcessor {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerChat()) return;

        PlayerModel player = getPlayerModel(event.player);
        String rawMessage = IChatComponent.Serializer.func_150696_a(event.component);

        PlayerChatEvent forgeServerChatEvent = new PlayerChatEvent(player, "", rawMessage, event.message);
        GlobalContext.sendEvent(forgeServerChatEvent);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerJoin()) return;
        PlayerModel player = getPlayerModel((EntityPlayerMP) event.player);
        PlayerJoinEvent forgePlayerLoggedInEvent = new PlayerJoinEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedInEvent);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerLoggedOutEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;
        PlayerModel player = getPlayerModel((EntityPlayerMP) event.player);
        PlayerQuitEvent forgePlayerLoggedOutEvent = new PlayerQuitEvent(player);
        GlobalContext.sendEvent(forgePlayerLoggedOutEvent);
    }

    @SubscribeEvent
    public void onPlayerCommand(CommandEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerCommand()) return;
        if (!(event.sender instanceof EntityPlayerMP)) return;

        ICommand iCommand = event.command;

        String command = isRegisterOrLoginCommand(iCommand.toString());
        if (command.isEmpty()) return;

        PlayerModel player = getPlayerModel((EntityPlayerMP) event.sender);
        PlayerCommandEvent forgeCommandEvent = new PlayerCommandEvent(player, "", command, command);
        GlobalContext.sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
        if (!(event.entityLiving instanceof EntityPlayerMP)) return;

        IChatComponent component = event.source.func_151519_b(event.entityLiving);
        String reason = event.source.func_151519_b(event.entityLiving).getUnformattedText();

        DeathModel death = new DeathModel();
        death.setText(reason);

        if (component instanceof ChatComponentTranslation chatComponentTranslation) {
            String key = chatComponentTranslation.getKey();
            String[] args = Arrays.stream(chatComponentTranslation.getFormatArgs()).map(obj -> {
                if (obj instanceof IChatComponent) {
                    return ((IChatComponent) obj).getUnformattedText();
                } else {
                    return String.valueOf(obj);
                }
            }).toArray(String[]::new);
            death.setKey(key);
            death.setArgs(args);
        }

        PlayerModel player = getPlayerModel((EntityPlayerMP) event.entityLiving);
        PlayerDeathEvent forgeCommandEvent = new PlayerDeathEvent(player, death);
        GlobalContext.sendEvent(forgeCommandEvent);
    }

    @SubscribeEvent
    public void onPlayerAdvancementEvent(AchievementEvent event) {
        if (event.isCanceled() || !GlobalContext.getConfig().getSubscribeEvent().isPlayerAdvancement()) return;
        Achievement achievement = event.achievement;
        if (achievement.statId == null || achievement.statId.isEmpty() || achievement.statId.equals("achievement.openInventory"))
            return;
//        if (achievement.getDescription() == null || achievement.getDescription().isEmpty()) return;  // Client Only
        if (achievement.func_150951_e() == null) return;


        PlayerModel playerModel = getPlayerModel((EntityPlayerMP) event.entityPlayer);

        AchievementModel achievementModel = getForgeAchievement(achievement);

        String text = achievementModel.pattern(achievementModel.getDisplay().getFrame(), playerModel.getNickname(), "[" + achievement.func_150951_e().getUnformattedText() + "]");
        achievementModel.setText(text);

        PlayerAchievementEvent forgeAdvancementEvent = new PlayerAchievementEvent(playerModel, achievementModel);
        GlobalContext.sendEvent(forgeAdvancementEvent);
    }

}
