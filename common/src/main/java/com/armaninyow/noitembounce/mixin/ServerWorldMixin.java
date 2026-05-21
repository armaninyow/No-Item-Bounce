package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.BlockDropTracker;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
	private int cleanupCounter = 0;

	@Inject(method = "tick", at = @At("TAIL"))
	private void onWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		// Remove only stale entries (older than 5 seconds) every 100 ticks,
		// so two nearby block breaks don't clear each other's tracked positions.
		cleanupCounter++;
		if (cleanupCounter >= 100) {
			BlockDropTracker.clearStale();
			cleanupCounter = 0;
		}
	}
}