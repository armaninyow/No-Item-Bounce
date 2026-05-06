package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.MobDeathTracker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDeathMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        MobDeathTracker.markMobDying(self.getUuid(), new Vec3d(self.getX(), self.getY(), self.getZ()));
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void afterEntityDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        MobDeathTracker.clearMob(self.getUuid());
    }
}