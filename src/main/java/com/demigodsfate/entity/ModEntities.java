package com.demigodsfate.entity;

import com.demigodsfate.DemigodsFate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all custom entities in the mod.
 * Entities are added as they're implemented in phases.
 */
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, DemigodsFate.MODID);

    // Entities will be registered here as they're implemented.
    // Phase 4 will add: Minotaur, Hellhound, Empousai, Dracanae, etc.
}
