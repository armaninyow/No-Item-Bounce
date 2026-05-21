package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.MobDeathTracker;
import com.armaninyow.noitembounce.PlayerDeathTracker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerDeathMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        Vec3 pos = new Vec3(self.getX(), self.getY(), self.getZ());
        PlayerDeathTracker.markPlayerDying(self.getUUID());
        MobDeathTracker.markPlayerDying(self.getUUID(), pos);
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void afterPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        PlayerDeathTracker.clearPlayer(self.getUUID());
        MobDeathTracker.clearMob(self.getUUID());
    }
}