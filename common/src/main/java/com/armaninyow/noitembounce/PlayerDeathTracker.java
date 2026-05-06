package com.armaninyow.noitembounce;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerDeathTracker {
    private static final Set<UUID> dyingPlayers = new HashSet<>();

    public static void markPlayerDying(UUID uuid) {
        dyingPlayers.add(uuid);
    }

    public static boolean isPlayerDying(UUID uuid) {
        return dyingPlayers.contains(uuid);
    }

    public static void clearPlayer(UUID uuid) {
        dyingPlayers.remove(uuid);
    }
}