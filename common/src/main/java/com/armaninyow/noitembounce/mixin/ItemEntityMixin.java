package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.BlockDropTracker;
import com.armaninyow.noitembounce.IVelocityLockable;
import com.armaninyow.noitembounce.MobDeathTracker;
import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.PlayerDeathTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements IVelocityLockable {

    @Unique
    private boolean noitembounce$velocityLocked = false;

    @Override
    public boolean noitembounce$isVelocityLocked() {
        return noitembounce$velocityLocked;
    }

    @Override
    public void noitembounce$setVelocityLocked(boolean locked) {
        noitembounce$velocityLocked = locked;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At("RETURN"))
    private void onItemEntityInit(Level level, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;

        double velocityY = self.getDeltaMovement().y;
        BlockPos blockPos = BlockPos.containing(x, y, z);
        boolean isBlockDrop = BlockDropTracker.isTrackedPosition(blockPos);

        if (isBlockDrop) {
            double centeredX = Math.floor(x) + 0.5;
            double centeredZ = Math.floor(z) + 0.5;

            if (NoItemBounce.shouldRemoveVerticalBounce()) {
                double bottomY = Math.floor(y);
                self.setPos(centeredX, bottomY, centeredZ);
                self.setDeltaMovement(0.0, 0.0, 0.0);
            } else {
                self.setPos(centeredX, y, centeredZ);
                self.setDeltaMovement(0.0, velocityY, 0.0);
            }

            noitembounce$velocityLocked = true;

        } else {
            // Mob/player death drops
            MobDeathTracker.DeathEntry entry = MobDeathTracker.findNearbyDeathEntry(x, y, z);
            boolean shouldCenter = entry != null &&
                (entry.isPlayer ? PlayerDeathTracker.isPlayerDying(entry.uuid) : true);

            if (shouldCenter) {
                if (NoItemBounce.shouldRemoveVerticalBounce()) {
                    double bottomY = Math.floor(y);
                    self.setPos(entry.pos.x, bottomY, entry.pos.z);
                    self.setDeltaMovement(0.0, 0.0, 0.0);
                } else {
                    self.setPos(entry.pos.x, y, entry.pos.z);
                    self.setDeltaMovement(0.0, velocityY, 0.0);
                }
                noitembounce$velocityLocked = true;
            }
        }
    }

    // Suppress the landing bounce (movement.multiply(1.0, -0.5, 1.0)) that
    // happens in tick() when an item hits the ground, if vertical bounce is disabled.
    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
            ordinal = 0
        )
    )
    private void suppressLandingBounce(ItemEntity self, Vec3 movement) {
        if (NoItemBounce.shouldRemoveVerticalBounce()) {
            // Zero out Y so the item stays on the ground instead of bouncing
            self.setDeltaMovement(movement.x, 0.0, movement.z);
        } else {
            self.setDeltaMovement(movement);
        }
    }
}