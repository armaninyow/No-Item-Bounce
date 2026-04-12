package com.armaninyow.noitembounce;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class StorageBlockTracker {
    private static final Set<BlockPos> brokenStorageBlocks = new HashSet<>();

    public static void markStorageBlockBroken(BlockPos pos) {
        brokenStorageBlocks.add(pos.toImmutable());
    }

    public static boolean isStorageBlockPosition(BlockPos pos) {
        return brokenStorageBlocks.contains(pos);
    }

    public static void clearStorageBlockPosition(BlockPos pos) {
        brokenStorageBlocks.remove(pos);
    }

    public static void clearAll() {
        brokenStorageBlocks.clear();
    }
}
