package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 1.21_1.21.1
@Mixin(ArmorStandEntity.class)
public class ArmorStandMixin {

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"))
    private void onArmorStandDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ArmorStandEntity self = (ArmorStandEntity) (Object) this;

        if (self.getHealth() - amount <= 0 && source.getAttacker() != null) {
            StorageBlockTracker.markStorageBlockBroken(self.getBlockPos());
        }
    }
}
