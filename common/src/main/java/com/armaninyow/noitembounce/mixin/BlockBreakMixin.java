package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		StorageBlockTracker.markStorageBlockBroken(pos);
	}

	// dropStacks is the common entry point for ALL block drops regardless of cause —
	// covers plants, leaves, tree logs, and anything that skips onBroken.
	@Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
	private static void onDropStacks(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
		StorageBlockTracker.markStorageBlockBroken(pos);
	}

	// dropStack (single item variant) is used by bamboo, sugar cane, and similar
	// blocks that pop off individually via block update logic.
	@Inject(method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	private static void onDropStack(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
		StorageBlockTracker.markStorageBlockBroken(pos);
	}
}