package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.VehicleDropTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartContainer.class)
public class MinecartContainerDestroyMixin {

    @Inject(method = "destroy", at = @At("HEAD"))
    private void onDestroy(ServerLevel level, DamageSource source, CallbackInfo ci) {
        AbstractMinecartContainer self = (AbstractMinecartContainer) (Object) this;
        VehicleDropTracker.markVehicleDropping(self.getUUID(), new Vec3(self.getX(), self.getY(), self.getZ()));
    }

    @Inject(method = "destroy", at = @At("TAIL"))
    private void afterDestroy(ServerLevel level, DamageSource source, CallbackInfo ci) {
        AbstractMinecartContainer self = (AbstractMinecartContainer) (Object) this;
        VehicleDropTracker.clearVehicle(self.getUUID());
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if (!reason.shouldDestroy()) return;
        AbstractMinecartContainer self = (AbstractMinecartContainer) (Object) this;
        if (self.level().isClientSide()) return;
        VehicleDropTracker.markVehicleDropping(self.getUUID(), new Vec3(self.getX(), self.getY(), self.getZ()));
    }

    @Inject(method = "remove", at = @At("TAIL"))
    private void afterRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        AbstractMinecartContainer self = (AbstractMinecartContainer) (Object) this;
        VehicleDropTracker.clearVehicle(self.getUUID());
    }
}