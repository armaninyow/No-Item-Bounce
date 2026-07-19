package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class VehicleItemDropMixin {

    @Inject(
        method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("RETURN")
    )
    private void onSpawnAtLocation(ServerLevel level, ItemStack itemStack, Vec3 offset, CallbackInfoReturnable<ItemEntity> cir) {
        Object self = (Object) this;
        if (!(self instanceof AbstractMinecartContainer) && !(self instanceof AbstractBoat)) return;

        ItemEntity itemEntity = cir.getReturnValue();
        if (itemEntity == null) return;

        Entity entity = (Entity) self;
        Vec3 pos = new Vec3(entity.getX(), entity.getY(), entity.getZ());

        if (NoItemBounce.shouldRemoveVerticalBounce()) {
            itemEntity.setPos(pos.x, Math.floor(pos.y), pos.z);
            itemEntity.setDeltaMovement(0.0, 0.0, 0.0);
        } else {
            itemEntity.setPos(pos.x, pos.y, pos.z);
            itemEntity.setDeltaMovement(0.0, itemEntity.getDeltaMovement().y, 0.0);
        }
    }
}