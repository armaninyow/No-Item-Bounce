package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockBreakMixin {
    
    @Inject(method = "onBroken", at = @At("HEAD"))
    private void onBlockBroken(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (world.isClient()) return;
        
        Block block = state.getBlock();
        
        // Check if this is a storage block we care about
        if (isTargetStorageBlock(block)) {
            // Mark this position
            StorageBlockTracker.markStorageBlockBroken(pos);
        }
    }
    
    private boolean isTargetStorageBlock(Block block) {
        // Standard Storage
        if (block instanceof ChestBlock) return true;
        if (block instanceof EnderChestBlock) return true;
        if (block instanceof BarrelBlock) return true;
        if (block instanceof ShulkerBoxBlock) return true;
        
        // Processing
        if (block instanceof AbstractFurnaceBlock) return true;
        if (block instanceof BrewingStandBlock) return true;
        if (block instanceof HopperBlock) return true;
        
        // Utility
        if (block instanceof DispenserBlock) return true;
        if (block instanceof DropperBlock) return true;
        if (block instanceof JukeboxBlock) return true;
        if (block instanceof LecternBlock) return true;
        if (block instanceof ChiseledBookshelfBlock) return true;
        
        // Decorative/Misc
        if (block instanceof DecoratedPotBlock) return true;
        if (block instanceof FlowerPotBlock) return true;
        if (block instanceof CampfireBlock) return true;
        
        return false;
    }
}