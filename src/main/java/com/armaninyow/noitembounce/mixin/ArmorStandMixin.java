package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public class ArmorStandMixin {
    
    @Inject(method = "damage", at = @At("HEAD"))
    private void onArmorStandDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ArmorStandEntity self = (ArmorStandEntity) (Object) this;
        
        // If the armor stand is about to die and was damaged by a player
        if (self.getHealth() - amount <= 0 && source.getAttacker() != null) {
            // Mark the position for item centering
            StorageBlockTracker.markStorageBlockBroken(self.getBlockPos());
        }
    }
}