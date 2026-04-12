package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemScatterer.class)
public class ItemScattererMixin {

	@Redirect(
		method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
		)
	)
	private static boolean redirectItemSpawn(World world, net.minecraft.entity.Entity entity) {
		if (!(entity instanceof ItemEntity itemEntity)) {
			return world.spawnEntity(entity);
		}

		BlockPos pos = itemEntity.getBlockPos();

		if (StorageBlockTracker.isStorageBlockPosition(pos)) {
			double x = itemEntity.getX();
			double z = itemEntity.getZ();
			double y = itemEntity.getY();

			double centeredX = Math.floor(x) + 0.5;
			double centeredZ = Math.floor(z) + 0.5;
			double finalY = y;

			if (NoItemBounce.shouldRemoveVerticalBounce()) {
				finalY = Math.floor(y);
			}

			itemEntity.setPosition(centeredX, finalY, centeredZ);
			itemEntity.setVelocity(0.0, NoItemBounce.shouldRemoveVerticalBounce() ? 0.0 : itemEntity.getVelocity().y, 0.0);
		}

		return world.spawnEntity(itemEntity);
	}
}
