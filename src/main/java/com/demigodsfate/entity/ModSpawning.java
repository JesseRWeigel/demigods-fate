package com.demigodsfate.entity;

import com.demigodsfate.DemigodsFate;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/**
 * Configure natural spawning for Percy Jackson monsters.
 * Monsters spawn in addition to vanilla mobs.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModSpawning {

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // Hellhounds spawn at night in dark areas (like vanilla hostile mobs)
        event.register(
                ModEntities.HELLHOUND.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        // Empousai spawn near villages (roads/plains)
        event.register(
                ModEntities.EMPOUSAI.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        // Furies spawn at night
        event.register(
                ModEntities.FURY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        // Cyclopes spawn in caves/mountains
        event.register(
                ModEntities.CYCLOPS.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }
}
