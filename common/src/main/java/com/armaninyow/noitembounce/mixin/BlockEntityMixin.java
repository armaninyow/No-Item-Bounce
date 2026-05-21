package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.BlockDropTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

	@Shadow
	public abstract Level getLevel();

	@Shadow
	public abstract BlockPos getBlockPos();

	@Inject(method = "setBlockState", at = @At("HEAD"))
	private void onBlockReplaced(BlockState state, CallbackInfo ci) {
		Level level = this.getLevel();
		if (level == null || level.isClientSide()) return;
		BlockDropTracker.markBlockBroken(this.getBlockPos());
	}
}