package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
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
		// If config is off, keep the original Y and velocityY (item will still hop up)
		
		// Apply the centered position
		self.setPosition(centeredX, finalY, centeredZ);
		
		// Set velocity to zero horizontally, and handle vertical based on config
		self.setVelocity(0.0, finalVelocityY, 0.0);
	}
}