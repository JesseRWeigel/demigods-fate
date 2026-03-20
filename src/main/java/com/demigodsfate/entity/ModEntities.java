package com.demigodsfate.entity;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.monster.MinotaurEntity;
import com.demigodsfate.entity.monster.HellhoundEntity;
import com.demigodsfate.entity.monster.FuryEntity;
import com.demigodsfate.entity.monster.MedusaEntity;
import com.demigodsfate.entity.monster.EmpousaiEntity;
import com.demigodsfate.entity.monster.CyclopsEntity;
import com.demigodsfate.entity.monster.ChimeraEntity;
import com.demigodsfate.entity.monster.HydraEntity;
import com.demigodsfate.entity.npc.CampNpcEntity;
import com.demigodsfate.entity.npc.ChironEntity;
import com.demigodsfate.entity.npc.MrDEntity;
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

    public static final DeferredHolder<EntityType<?>, EntityType<HellhoundEntity>> HELLHOUND =
            ENTITY_TYPES.register("hellhound",
                    () -> EntityType.Builder.of(HellhoundEntity::new, MobCategory.MONSTER)
                            .sized(0.8f, 0.9f) // Wolf-sized
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":hellhound"));

    public static final DeferredHolder<EntityType<?>, EntityType<FuryEntity>> FURY =
            ENTITY_TYPES.register("fury",
                    () -> EntityType.Builder.of(FuryEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.8f) // Humanoid-sized
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":fury"));

    public static final DeferredHolder<EntityType<?>, EntityType<MedusaEntity>> MEDUSA =
            ENTITY_TYPES.register("medusa",
                    () -> EntityType.Builder.of(MedusaEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":medusa"));

    public static final DeferredHolder<EntityType<?>, EntityType<EmpousaiEntity>> EMPOUSAI =
            ENTITY_TYPES.register("empousai",
                    () -> EntityType.Builder.of(EmpousaiEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":empousai"));

    public static final DeferredHolder<EntityType<?>, EntityType<CyclopsEntity>> CYCLOPS =
            ENTITY_TYPES.register("cyclops",
                    () -> EntityType.Builder.of(CyclopsEntity::new, MobCategory.MONSTER)
                            .sized(1.4f, 2.7f) // Large
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":cyclops"));

    public static final DeferredHolder<EntityType<?>, EntityType<ChimeraEntity>> CHIMERA =
            ENTITY_TYPES.register("chimera",
                    () -> EntityType.Builder.of(ChimeraEntity::new, MobCategory.MONSTER)
                            .sized(1.2f, 1.5f)
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":chimera"));

    public static final DeferredHolder<EntityType<?>, EntityType<HydraEntity>> HYDRA =
            ENTITY_TYPES.register("hydra",
                    () -> EntityType.Builder.of(HydraEntity::new, MobCategory.MONSTER)
                            .sized(2.0f, 2.5f) // Large boss
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":hydra"));

    // --- NPCs ---
    public static final DeferredHolder<EntityType<?>, EntityType<ChironEntity>> CHIRON =
            ENTITY_TYPES.register("chiron",
                    () -> EntityType.Builder.<ChironEntity>of(ChironEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":chiron"));

    public static final DeferredHolder<EntityType<?>, EntityType<MrDEntity>> MR_D =
            ENTITY_TYPES.register("mr_d",
                    () -> EntityType.Builder.<MrDEntity>of(MrDEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build(DemigodsFate.MODID + ":mr_d"));

    @EventBusSubscriber(modid = DemigodsFate.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(MINOTAUR.get(), MinotaurEntity.createAttributes().build());
            event.put(HELLHOUND.get(), HellhoundEntity.createAttributes().build());
            event.put(FURY.get(), FuryEntity.createAttributes().build());
            event.put(MEDUSA.get(), MedusaEntity.createAttributes().build());
            event.put(EMPOUSAI.get(), EmpousaiEntity.createAttributes().build());
            event.put(CYCLOPS.get(), CyclopsEntity.createAttributes().build());
            event.put(CHIMERA.get(), ChimeraEntity.createAttributes().build());
            event.put(HYDRA.get(), HydraEntity.createAttributes().build());
            event.put(CHIRON.get(), CampNpcEntity.createAttributes().build());
            event.put(MR_D.get(), CampNpcEntity.createAttributes().build());
        }
    }
}
