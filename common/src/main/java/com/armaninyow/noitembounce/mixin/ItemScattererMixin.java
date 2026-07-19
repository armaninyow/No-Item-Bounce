package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.BlockDropTracker;
import com.armaninyow.noitembounce.VehicleDropTracker;
import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.ShearTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Containers.class)
public class ItemScattererMixin {

	@Redirect(
		method = "dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
		)
	)
	private static boolean redirectItemSpawn(Level level, Entity entity) {
		if (!(entity instanceof ItemEntity itemEntity)) {
			return level.addFreshEntity(entity);
		}

		double x = itemEntity.getX();
		double y = itemEntity.getY();
		double z = itemEntity.getZ();
		BlockPos pos = itemEntity.blockPosition();

		// Check shear tracker first (for Mooshroom which bypasses spawnAtLocation)
		Vec3 shearPos = ShearTracker.findNearbyShearPos(x, y, z);
		if (shearPos != null) {
			if (NoItemBounce.shouldRemoveVerticalBounce()) {
				itemEntity.setPos(shearPos.x, Math.floor(shearPos.y), shearPos.z);
				itemEntity.setDeltaMovement(0.0, 0.0, 0.0);
			} else {
				itemEntity.setPos(shearPos.x, y, shearPos.z);
				itemEntity.setDeltaMovement(0.0, itemEntity.getDeltaMovement().y, 0.0);
			}
			return level.addFreshEntity(itemEntity);
		}

		// Check vehicle tracker (exact entity position for minecarts and boats)
		Vec3 vehiclePos = VehicleDropTracker.findNearbyDropPos(x, y, z);
		if (vehiclePos != null) {
			if (NoItemBounce.shouldRemoveVerticalBounce()) {
				itemEntity.setPos(vehiclePos.x, Math.floor(vehiclePos.y), vehiclePos.z);
				itemEntity.setDeltaMovement(0.0, 0.0, 0.0);
			} else {
				itemEntity.setPos(vehiclePos.x, y, vehiclePos.z);
				itemEntity.setDeltaMovement(0.0, itemEntity.getDeltaMovement().y, 0.0);
			}
			return level.addFreshEntity(itemEntity);
		}

		// Then check block drop tracker (for broken blocks / storage blocks)
		if (BlockDropTracker.isTrackedPosition(pos)) {
			if (NoItemBounce.shouldRemoveVerticalBounce()) {
				double bottomY = Math.floor(y);
				itemEntity.setPos(Math.floor(x) + 0.5, bottomY, Math.floor(z) + 0.5);
				itemEntity.setDeltaMovement(0.0, 0.0, 0.0);
			} else {
				itemEntity.setPos(Math.floor(x) + 0.5, y, Math.floor(z) + 0.5);
				itemEntity.setDeltaMovement(0.0, itemEntity.getDeltaMovement().y, 0.0);
			}
		}

		return level.addFreshEntity(itemEntity);
	}
}