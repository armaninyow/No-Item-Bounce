package com.armaninyow.noitembounce;

import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobDeathTracker {

    public static class DeathEntry {
        public final UUID uuid;
        public final Vec3 pos;
        public final boolean isPlayer;

        public DeathEntry(UUID uuid, Vec3 pos, boolean isPlayer) {
            this.uuid = uuid;
            this.pos = pos;
            this.isPlayer = isPlayer;
        }

        @Override
        public String toString() {
            return String.format("DeathEntry{uuid=%s, pos=(%s,%s,%s), isPlayer=%s}", uuid, pos.x, pos.y, pos.z, isPlayer);
        }
    }

    private static final Map<UUID, DeathEntry> dyingEntities = new HashMap<>();

    public static void markMobDying(UUID uuid, Vec3 pos) {
        dyingEntities.put(uuid, new DeathEntry(uuid, pos, false));
    }

    public static void markPlayerDying(UUID uuid, Vec3 pos) {
        dyingEntities.put(uuid, new DeathEntry(uuid, pos, true));
    }

    public static void clearMob(UUID uuid) {
        dyingEntities.remove(uuid);
    }

    public static void clearAll() {
        dyingEntities.clear();
    }

    public static DeathEntry findNearbyDeathEntry(double x, double y, double z) {
        for (DeathEntry entry : dyingEntities.values()) {
            double dx = entry.pos.x - x;
            double dy = entry.pos.y - y;
            double dz = entry.pos.z - z;
            if (dx * dx + dy * dy + dz * dz <= 4.0) {
                return entry;
            }
        }
        return null;
    }

    public static Vec3 findNearbyDeathPos(double x, double y, double z) {
        DeathEntry entry = findNearbyDeathEntry(x, y, z);
        return entry != null ? entry.pos : null;
    }
}