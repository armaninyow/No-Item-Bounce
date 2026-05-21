package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.BlockDropTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockBreakMixin {

	@Inject(method = "destroy", at = @At("HEAD"))
	private void onBlockBroken(LevelAccessor level, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (level.isClientSide()) return;
		BlockDropTracker.markBlockBroken(pos);
	}

	@Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", at = @At("HEAD"))
	private static void onDropResources(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
		BlockDropTracker.markBlockBroken(pos);
	}

	@Inject(method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
	private static void onPopResource(Level level, BlockPos pos, ItemStack stack, CallbackInfo ci) {
		BlockDropTracker.markBlockBroken(pos);
	}
}