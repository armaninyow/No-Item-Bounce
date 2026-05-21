package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.IVelocityLockable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityVelocityMixin {

    @Inject(method = "setDeltaMovement(DDD)V", at = @At("HEAD"), cancellable = true)
    private void onSetDeltaMovement(double x, double y, double z, CallbackInfo ci) {
        if (!((Object) this instanceof ItemEntity)) return;
        IVelocityLockable lockable = (IVelocityLockable)(Object) this;
        if (lockable.noitembounce$isVelocityLocked()) {
            ci.cancel();
            lockable.noitembounce$setVelocityLocked(false);
        }
    }

    @Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    private void onSetDeltaMovementVec(Vec3 velocity, CallbackInfo ci) {
        if (!((Object) this instanceof ItemEntity)) return;
        IVelocityLockable lockable = (IVelocityLockable)(Object) this;
        if (lockable.noitembounce$isVelocityLocked()) {
            ci.cancel();
            lockable.noitembounce$setVelocityLocked(false);
        }
    }
}