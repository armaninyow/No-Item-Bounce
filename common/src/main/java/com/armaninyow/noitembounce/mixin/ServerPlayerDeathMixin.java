package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.MobDeathTracker;
import com.armaninyow.noitembounce.PlayerDeathTracker;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerDeathMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        Vec3d pos = new Vec3d(self.getX(), self.getY(), self.getZ());
        PlayerDeathTracker.markPlayerDying(self.getUuid());
        MobDeathTracker.markPlayerDying(self.getUuid(), pos);
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void afterPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        PlayerDeathTracker.clearPlayer(self.getUuid());
        MobDeathTracker.clearMob(self.getUuid());
    }
}