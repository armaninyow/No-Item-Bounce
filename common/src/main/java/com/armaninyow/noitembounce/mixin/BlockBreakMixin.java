package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockBreakMixin {

	@Inject(method = "onBroken", at = @At("HEAD"))
	private void onBlockBroken(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.isClient()) return;

		if (NoItemBounce.isTargetStorageBlock(state.getBlock())) {
			StorageBlockTracker.markStorageBlockBroken(pos);
		}
	}
}
