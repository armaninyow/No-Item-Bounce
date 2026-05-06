package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.IVelocityLockable;
import com.armaninyow.noitembounce.MobDeathTracker;
import com.armaninyow.noitembounce.NoItemBounce;
import com.armaninyow.noitembounce.PlayerDeathTracker;
import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
        boolean isStorageBlockItem = StorageBlockTracker.isStorageBlockPosition(blockPos);

        NoItemBounce.LOGGER.info("[ItemEntityMixin] 5-param: item={} pos=({}, {}, {}) vel=({}, {}, {}) isStorage={}",
            stack.getItem().getTranslationKey(), x, y, z,
            self.getVelocity().x, velocityY, self.getVelocity().z, isStorageBlockItem);

        double finalVelocityY = NoItemBounce.shouldRemoveVerticalBounce() ? 0.0 : velocityY;

        if (isStorageBlockItem) {
            // Storage block drops: center on block middle
            double centeredX = Math.floor(x) + 0.5;
            double centeredZ = Math.floor(z) + 0.5;
            self.setPosition(centeredX, y, centeredZ);
            self.setVelocity(0.0, finalVelocityY, 0.0);
            noitembounce$velocityLocked = true;
            NoItemBounce.LOGGER.info("[ItemEntityMixin] -> storage locked at ({}, {}, {})", centeredX, y, centeredZ);

        } else {
            // Mob/player death drops: center on entity position, but only if actually dying
            MobDeathTracker.DeathEntry entry = MobDeathTracker.findNearbyDeathEntry(x, y, z);
            boolean shouldCenter = entry != null &&
                (entry.isPlayer ? PlayerDeathTracker.isPlayerDying(entry.uuid) : true);

            NoItemBounce.LOGGER.info("[ItemEntityMixin] -> entry={} shouldCenter={}", entry, shouldCenter);

            if (shouldCenter) {
                self.setPosition(entry.pos.x, y, entry.pos.z);
                self.setVelocity(0.0, finalVelocityY, 0.0);
                noitembounce$velocityLocked = true;
                NoItemBounce.LOGGER.info("[ItemEntityMixin] -> death/mob locked at ({}, {}, {})", entry.pos.x, y, entry.pos.z);
            }
            // else: Q-drops, thrown items, etc. — leave completely untouched
        }
    }
}