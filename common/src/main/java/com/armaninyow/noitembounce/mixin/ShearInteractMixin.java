package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.ShearTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class ShearInteractMixin {

    @Inject(method = "shear", at = @At("HEAD"), require = 0)
    private void onShearHead(ServerLevel level, SoundSource soundSource, ItemStack tool, CallbackInfo ci) {
        if (!((Object) this instanceof Shearable)) return;
        LivingEntity self = (LivingEntity) (Object) this;
        ShearTracker.markShearing(self.getUUID(), new Vec3(self.getX(), self.getY(), self.getZ()));
    }

    @Inject(method = "shear", at = @At("TAIL"), require = 0)
    private void onShearTail(ServerLevel level, SoundSource soundSource, ItemStack tool, CallbackInfo ci) {
        if (!((Object) this instanceof Shearable)) return;
        LivingEntity self = (LivingEntity) (Object) this;
        ShearTracker.clearShearing(self.getUUID());
    }
}