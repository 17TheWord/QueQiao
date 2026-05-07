package com.github.theword.queqiao.mixin;

import com.github.theword.queqiao.tool.GlobalContext;
import com.github.theword.queqiao.tool.event.PlayerDeathEvent;
import com.github.theword.queqiao.tool.event.model.TranslateModel;
import com.github.theword.queqiao.utils.FabricTool;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.theword.queqiao.utils.FabricTool.getFabricPlayer;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin {


    @Inject(method = "die", at = @At("TAIL"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        if (!GlobalContext.getConfig().getSubscribeEvent().isPlayerDeath()) return;

        ServerPlayer player = (ServerPlayer) (Object) this;
        Component localizedDeathMessage = source.getLocalizedDeathMessage(player);

        TranslateModel translateModel = FabricTool.parseTranslateModel(localizedDeathMessage);

        PlayerDeathEvent event = new PlayerDeathEvent(getFabricPlayer(player), translateModel);
        GlobalContext.sendEvent(event);
    }

}