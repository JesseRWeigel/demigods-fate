package com.demigodsfate.world;

import com.demigodsfate.DemigodsFate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages camp safe zones where hostile mobs cannot spawn or enter.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class CampSafeZone {
    private static final List<SafeZone> safeZones = new ArrayList<>();

    public static void addSafeZone(BlockPos center, int radius, String name) {
        safeZones.add(new SafeZone(center, radius, name));
        DemigodsFate.LOGGER.info("Camp safe zone created: {} at {} radius {}", name, center, radius);
    }

    public static boolean isInSafeZone(BlockPos pos) {
        for (SafeZone zone : safeZones) {
            double distSq = pos.distSqr(zone.center);
            if (distSq <= (double) zone.radius * zone.radius) {
                return true;
            }
        }
        return false;
    }

    public static List<SafeZone> getSafeZones() {
        return safeZones;
    }

    /**
     * Cancel hostile mobs joining the world inside safe zones.
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Monster monster && !event.getLevel().isClientSide()) {
            if (isInSafeZone(monster.blockPosition())) {
                event.setCanceled(true);
            }
        }
    }

    public record SafeZone(BlockPos center, int radius, String name) {}
}
