package com.armaninyow.noitembounce;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class BlockDropTracker {
    // Maps block position to the time (in ms) it was marked, for stale-entry cleanup
    private static final Map<BlockPos, Long> trackedBlocks = new HashMap<>();

    public static void markBlockBroken(BlockPos pos) {
        trackedBlocks.put(pos.immutable(), System.currentTimeMillis());
    }

    public static boolean isTrackedPosition(BlockPos pos) {
        return trackedBlocks.containsKey(pos);
    }

    public static void clearPosition(BlockPos pos) {
        trackedBlocks.remove(pos);
    }

    // Remove entries older than 5 seconds (5000ms) instead of clearing everything,
    // so two nearby block breaks don't interfere with each other.
    public static void clearStale() {
        long cutoff = System.currentTimeMillis() - 5000;
        trackedBlocks.entrySet().removeIf(entry -> entry.getValue() < cutoff);
    }

    public static void clearAll() {
        trackedBlocks.clear();
    }
}