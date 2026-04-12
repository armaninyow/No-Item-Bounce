package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

	@Shadow
	public abstract World getWorld();

	@Shadow
	public abstract BlockPos getPos();

	@Inject(method = "onBlockReplaced", at = @At("HEAD"))
	private void onBlockReplaced(BlockPos pos, BlockState state, CallbackInfo ci) {
		World world = this.getWorld();

		if (world == null || world.isClient()) return;

		if (NoItemBounce.isTargetStorageBlock(state.getBlock())) {
			StorageBlockTracker.markStorageBlockBroken(pos);
		}
	}
}
