package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.MobDeathTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDeathMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        MobDeathTracker.markMobDying(self.getUUID(), new Vec3(self.getX(), self.getY(), self.getZ()));
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void afterEntityDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        MobDeathTracker.clearMob(self.getUUID());
    }
}