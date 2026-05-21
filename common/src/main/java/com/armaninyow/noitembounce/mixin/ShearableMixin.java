package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.IVelocityLockable;
import com.armaninyow.noitembounce.NoItemBounce;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ShearableMixin {

    @Inject(
        method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("RETURN")
    )
    private void onSpawnAtLocation(ServerLevel level, ItemStack itemStack, Vec3 offset, CallbackInfoReturnable<ItemEntity> cir) {
        if (!((Object) this instanceof Shearable)) return;

        ItemEntity itemEntity = cir.getReturnValue();
        if (itemEntity == null) return;

        Entity self = (Entity) (Object) this;
        double x = self.getX();
        double y = self.getY();
        double z = self.getZ();

        if (NoItemBounce.shouldRemoveVerticalBounce()) {
            double bottomY = Math.floor(y);
            itemEntity.setPos(x, bottomY, z);
            itemEntity.setDeltaMovement(0.0, 0.0, 0.0);
        } else {
            itemEntity.setPos(x, y, z);
            itemEntity.setDeltaMovement(0.0, itemEntity.getDeltaMovement().y, 0.0);
        }

        // Lock velocity so Sheep's post-spawn setDeltaMovement scatter call is blocked
        ((IVelocityLockable) itemEntity).noitembounce$setVelocityLocked(true);
    }
}