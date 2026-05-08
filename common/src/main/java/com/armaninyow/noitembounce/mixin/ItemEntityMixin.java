package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.IVelocityLockable;
import com.armaninyow.noitembounce.MobDeathTracker;
import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.PlayerDeathTracker;
import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
    private void onItemEntityInit(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;

        double velocityY = self.getVelocity().y;
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        boolean isBlockItem = StorageBlockTracker.isStorageBlockPosition(blockPos);

        NoItemBounce.LOGGER.info("[ItemEntityMixin] 5-param: item={} pos=({}, {}, {}) vel=({}, {}, {}) isBlock={}",
            stack.getItem().getTranslationKey(), x, y, z,
            self.getVelocity().x, velocityY, self.getVelocity().z, isBlockItem);

        if (isBlockItem) {
            double centeredX = Math.floor(x) + 0.5;
            double centeredZ = Math.floor(z) + 0.5;

            if (NoItemBounce.shouldRemoveVerticalBounce()) {
                // Place item at the bottom of the broken block's space with zero Y velocity.
                // It appears already landed — no bounce up, no fall down.
                double bottomY = Math.floor(y);
                self.setPosition(centeredX, bottomY, centeredZ);
                self.setVelocity(0.0, 0.0, 0.0);
            } else {
                self.setPosition(centeredX, y, centeredZ);
                self.setVelocity(0.0, velocityY, 0.0);
            }

            noitembounce$velocityLocked = true;
            NoItemBounce.LOGGER.info("[ItemEntityMixin] -> block drop locked at ({}, {}, {})", centeredX, y, centeredZ);

        } else {
            // Mob/player death drops
            MobDeathTracker.DeathEntry entry = MobDeathTracker.findNearbyDeathEntry(x, y, z);
            boolean shouldCenter = entry != null &&
                (entry.isPlayer ? PlayerDeathTracker.isPlayerDying(entry.uuid) : true);

            NoItemBounce.LOGGER.info("[ItemEntityMixin] -> entry={} shouldCenter={}", entry, shouldCenter);

            if (shouldCenter) {
                if (NoItemBounce.shouldRemoveVerticalBounce()) {
                    double bottomY = Math.floor(y);
                    self.setPosition(entry.pos.x, bottomY, entry.pos.z);
                    self.setVelocity(0.0, 0.0, 0.0);
                } else {
                    self.setPosition(entry.pos.x, y, entry.pos.z);
                    self.setVelocity(0.0, velocityY, 0.0);
                }
                noitembounce$velocityLocked = true;
                NoItemBounce.LOGGER.info("[ItemEntityMixin] -> death/mob locked at ({}, {}, {})", entry.pos.x, y, entry.pos.z);
            }
            // else: Q-drops, thrown items, etc. — leave completely untouched
        }
    }
}