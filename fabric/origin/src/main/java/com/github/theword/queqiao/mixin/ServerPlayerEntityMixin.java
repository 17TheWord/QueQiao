package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerDeathEvent;
import com.github.theword.queqiao.tool.event.PlayerQuitEvent;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.utils.FabricTool;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        Text deathMessage = player.getDamageTracker().getDeathMessage();

        TranslateModel translateModel = FabricTool.parseTranslateModel(deathMessage);

        PlayerDeathEvent event = new PlayerDeathEvent(getFabricPlayer(player), translateModel);
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