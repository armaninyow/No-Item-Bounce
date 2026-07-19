package com.armaninyow.noitembounce;

import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VehicleDropTracker {
    private static final Map<UUID, Vec3> droppingVehicles = new HashMap<>();

    public static void markVehicleDropping(UUID uuid, Vec3 pos) {
        droppingVehicles.put(uuid, pos);
    }

    public static void clearVehicle(UUID uuid) {
        droppingVehicles.remove(uuid);
    }

    public static Vec3 findNearbyDropPos(double x, double y, double z) {
        for (Map.Entry<UUID, Vec3> entry : droppingVehicles.entrySet()) {
            Vec3 pos = entry.getValue();
            double dx = pos.x - x;
            double dy = pos.y - y;
            double dz = pos.z - z;
            if (dx * dx + dy * dy + dz * dz <= 4.0) {
                return pos;
            }
        }
        return null;
    }
}