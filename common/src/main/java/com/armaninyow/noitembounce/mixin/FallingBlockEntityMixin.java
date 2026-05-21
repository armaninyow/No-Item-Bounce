package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.BlockDropTracker;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        FallingBlockEntity self = (FallingBlockEntity) (Object) this;
        BlockDropTracker.markBlockBroken(self.blockPosition());
    }
}