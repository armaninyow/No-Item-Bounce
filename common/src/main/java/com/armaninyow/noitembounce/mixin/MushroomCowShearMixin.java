package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.NoItemBounce;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MushroomCow.class)
public class MushroomCowShearMixin {

    @Inject(method = "shear", at = @At("TAIL"))
    private void onShearTail(ServerLevel level, SoundSource soundSource, ItemStack tool, CallbackInfo ci) {
        MushroomCow self = (MushroomCow) (Object) this;

        double x = self.getX();
        double y = self.getY();
        double z = self.getZ();

        // Scan for item entities that just spawned near the mooshroom's position.
        // Since convertTo is synchronous, the mushroom drops are already in the world by TAIL.
        AABB searchBox = new AABB(x - 2, y - 2, z - 2, x + 2, y + 2, z + 2);
        List<ItemEntity> nearby = level.getEntitiesOfClass(ItemEntity.class, searchBox);

        for (ItemEntity itemEntity : nearby) {
            if (NoItemBounce.shouldRemoveVerticalBounce()) {
                itemEntity.setPos(x, Math.floor(y), z);
                itemEntity.setDeltaMovement(0.0, 0.0, 0.0);
            } else {
                itemEntity.setPos(x, y, z);
                itemEntity.setDeltaMovement(0.0, itemEntity.getDeltaMovement().y, 0.0);
            }
        }
    }
}