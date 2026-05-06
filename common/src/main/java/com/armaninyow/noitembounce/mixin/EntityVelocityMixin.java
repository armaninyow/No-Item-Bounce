package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.IVelocityLockable;
import com.armaninyow.noitembounce.NoItemBounce;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityVelocityMixin {

    @Inject(method = "setVelocity(DDD)V", at = @At("HEAD"), cancellable = true)
    private void onSetVelocity(double x, double y, double z, CallbackInfo ci) {
        if (!((Object) this instanceof ItemEntity)) return;
        IVelocityLockable lockable = (IVelocityLockable)(Object) this;
        if (lockable.noitembounce$isVelocityLocked()) {
            NoItemBounce.LOGGER.info("[EntityVelocityMixin] blocked setVelocity(DDD)({}, {}, {})", x, y, z);
            ci.cancel();
            lockable.noitembounce$setVelocityLocked(false);
        }
    }

    @Inject(method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    private void onSetVelocityVec(Vec3d velocity, CallbackInfo ci) {
        if (!((Object) this instanceof ItemEntity)) return;
        IVelocityLockable lockable = (IVelocityLockable)(Object) this;
        if (lockable.noitembounce$isVelocityLocked()) {
            NoItemBounce.LOGGER.info("[EntityVelocityMixin] blocked setVelocity(Vec3d)({}, {}, {})", velocity.x, velocity.y, velocity.z);
            ci.cancel();
            lockable.noitembounce$setVelocityLocked(false);
        }
    }
}