package com.demigodsfate.entity;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.monster.MinotaurEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, DemigodsFate.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<MinotaurEntity>> MINOTAUR =
            ENTITY_TYPES.register("minotaur",
                    () -> EntityType.Builder.of(MinotaurEntity::new, MobCategory.MONSTER)
                            .sized(1.95f, 2.2f) // Slightly larger than player
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":minotaur"));

    @EventBusSubscriber(modid = DemigodsFate.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(MINOTAUR.get(), MinotaurEntity.createAttributes().build());
        }
    }
}
