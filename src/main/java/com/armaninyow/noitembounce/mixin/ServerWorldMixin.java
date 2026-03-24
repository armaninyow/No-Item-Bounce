package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    private int cleanupCounter = 0;
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void onWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        // Clean up tracked positions every 100 ticks (5 seconds)
        // This prevents memory leaks from positions that never spawned items
        cleanupCounter++;
        if (cleanupCounter >= 100) {
            StorageBlockTracker.clearAll();
            cleanupCounter = 0;
        }
    }
}