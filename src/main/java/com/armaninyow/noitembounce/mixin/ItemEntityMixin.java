package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V", at = @At("RETURN"))
	private void onItemEntityInit(World world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		
		// Check if this item is spawning from a broken storage block
		BlockPos blockPos = BlockPos.ofFloored(x, y, z);
		boolean isStorageBlockItem = StorageBlockTracker.isStorageBlockPosition(blockPos);
		
		// Center the item on X and Z axes (e.g., 10.5, 20.5)
		double centeredX = Math.floor(x) + 0.5;
		double centeredZ = Math.floor(z) + 0.5;
		
		// Handle Y position based on config
		double finalY = y;
		double finalVelocityY = velocityY;
		
		if (NoItemBounce.shouldRemoveVerticalBounce()) {
			// Remove vertical bounce - place at floor level
			finalY = Math.floor(y);
			finalVelocityY = 0.0;
		}
		
		// If this is from a storage block, ensure it spawns centered regardless
		if (isStorageBlockItem) {
			// For storage blocks, always center the position
			self.setPosition(centeredX, finalY, centeredZ);
			self.setVelocity(0.0, finalVelocityY, 0.0);
			
			NoItemBounce.LOGGER.debug("Storage block item spawned: {} at ({}, {}, {})", 
				stack.getItem().getTranslationKey(), centeredX, finalY, centeredZ);
		} else {
			// Regular item handling
			self.setPosition(centeredX, finalY, centeredZ);
			self.setVelocity(0.0, finalVelocityY, 0.0);
		}
	}
}