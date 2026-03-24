package com.armaninyow.noitembounce.mixin;

import com.armaninyow.noitembounce.StorageBlockTracker;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    
    @Shadow
    public abstract World getWorld();
    
    @Shadow
    public abstract BlockPos getPos();
    
    @Inject(method = "onBlockReplaced", at = @At("HEAD"))
    private void onBlockReplaced(BlockPos pos, BlockState state, CallbackInfo ci) {
        World world = this.getWorld();
        
        if (world == null || world.isClient()) return;
        
        Block block = state.getBlock();
        
        // Check if this is a storage block we care about
        if (isTargetStorageBlock(block)) {
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