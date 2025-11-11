package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerDeathEvent;
import com.github.theword.queqiao.tool.event.PlayerQuitEvent;
import com.github.theword.queqiao.tool.event.model.death.DeathModel;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
// IF < fabric-1.19.0
//import net.minecraft.text.TranslatableText;
// ELSE
//import net.minecraft.text.TranslatableTextContent;
// END IF
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.Arrays;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        DeathModel deathModel = new DeathModel();

        Text deathMessage = player.getDamageTracker().getDeathMessage();

        // IF < fabric-1.19.0
//        if (deathMessage instanceof TranslatableText) {
//            TranslatableText translatableText = (TranslatableText) deathMessage;
            // ELSE
//        if (deathMessage instanceof TranslatableTextContent) {
//            TranslatableTextContent translatableText = (TranslatableTextContent) deathMessage;
            // END IF
            deathModel.setKey(translatableText.getKey());
            String[] args = Arrays.stream(translatableText.getArgs()).map(obj -> {
                if (obj instanceof Text) {
                    return ((Text) obj).getString();
                } else {
                    return String.valueOf(obj);
                }
            }).toArray(String[]::new);
            deathModel.setArgs(args);
        }
        deathModel.setText(deathMessage.getString());

        PlayerDeathEvent event = new PlayerDeathEvent(getFabricPlayer(player), deathModel);
        GlobalContext.sendEvent(event);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnected(CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerQuit()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerQuitEvent event = new PlayerQuitEvent(getFabricPlayer(player));
        GlobalContext.sendEvent(event);
    }

}