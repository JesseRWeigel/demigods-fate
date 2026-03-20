package com.demigodsfate.client.renderer;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.ModEntities;
import com.demigodsfate.entity.monster.MinotaurEntity;
import com.demigodsfate.entity.monster.HellhoundEntity;
import com.demigodsfate.entity.monster.FuryEntity;
import com.demigodsfate.entity.monster.MedusaEntity;
import com.demigodsfate.entity.monster.EmpousaiEntity;
import com.demigodsfate.entity.monster.CyclopsEntity;
import com.demigodsfate.entity.npc.ChironEntity;
import com.demigodsfate.entity.npc.MrDEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Registers entity renderers using vanilla mob models as placeholders.
 * Placeholder mapping:
 * - Minotaur → scaled-up zombie (large bipedal)
 * - Hellhound → wolf renderer
 * - Fury → zombie renderer (humanoid)
 * - Medusa → zombie renderer (humanoid)
 */
@EventBusSubscriber(modid = DemigodsFate.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Use simple humanoid renderers as placeholders
        // All monsters render as zombies/husks with different textures until custom models are added
        event.registerEntityRenderer(ModEntities.MINOTAUR.get(), MinotaurRenderer::new);
        event.registerEntityRenderer(ModEntities.HELLHOUND.get(), HellhoundRenderer::new);
        event.registerEntityRenderer(ModEntities.FURY.get(), FuryRenderer::new);
        event.registerEntityRenderer(ModEntities.MEDUSA.get(), MedusaRenderer::new);
        event.registerEntityRenderer(ModEntities.EMPOUSAI.get(), EmpousaiRenderer::new);
        event.registerEntityRenderer(ModEntities.CYCLOPS.get(), CyclopsRenderer::new);
        event.registerEntityRenderer(ModEntities.CHIRON.get(), ChironRenderer::new);
        event.registerEntityRenderer(ModEntities.MR_D.get(), MrDRenderer::new);
    }

    // Minotaur — uses scaled-up humanoid model
    public static class MinotaurRenderer extends MobRenderer<MinotaurEntity, HumanoidModel<MinotaurEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/zombie/husk.png");

        public MinotaurRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 1.2f);
        }

        @Override
        public ResourceLocation getTextureLocation(MinotaurEntity entity) { return TEXTURE; }
    }

    // Hellhound — uses humanoid model (dark-themed)
    public static class HellhoundRenderer extends MobRenderer<HellhoundEntity, HumanoidModel<HellhoundEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");

        public HellhoundRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        }

        @Override
        public ResourceLocation getTextureLocation(HellhoundEntity entity) { return TEXTURE; }
    }

    // Fury — uses humanoid model
    public static class FuryRenderer extends MobRenderer<FuryEntity, HumanoidModel<FuryEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png");

        public FuryRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        }

        @Override
        public ResourceLocation getTextureLocation(FuryEntity entity) { return TEXTURE; }
    }

    // Medusa — uses humanoid model
    public static class MedusaRenderer extends MobRenderer<MedusaEntity, HumanoidModel<MedusaEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie_villager/zombie_villager.png");

        public MedusaRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        }

        @Override
        public ResourceLocation getTextureLocation(MedusaEntity entity) { return TEXTURE; }
    }

    // Empousai — humanoid (disguised as villager)
    public static class EmpousaiRenderer extends MobRenderer<EmpousaiEntity, HumanoidModel<EmpousaiEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie_villager/zombie_villager.png");

        public EmpousaiRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        }

        @Override
        public ResourceLocation getTextureLocation(EmpousaiEntity entity) { return TEXTURE; }
    }

    // Chiron — uses villager-like humanoid
    public static class ChironRenderer extends MobRenderer<ChironEntity, HumanoidModel<ChironEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/villager/villager.png");

        public ChironRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        }

        @Override
        public ResourceLocation getTextureLocation(ChironEntity entity) { return TEXTURE; }
    }

    // Mr. D — uses villager-like humanoid
    public static class MrDRenderer extends MobRenderer<MrDEntity, HumanoidModel<MrDEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/villager/villager.png");

        public MrDRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        }

        @Override
        public ResourceLocation getTextureLocation(MrDEntity entity) { return TEXTURE; }
    }

    // Cyclops — large humanoid
    public static class CyclopsRenderer extends MobRenderer<CyclopsEntity, HumanoidModel<CyclopsEntity>> {
        private static final ResourceLocation TEXTURE =
                ResourceLocation.withDefaultNamespace("textures/entity/iron_golem/iron_golem.png");

        public CyclopsRenderer(EntityRendererProvider.Context ctx) {
            super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 1.0f);
        }

        @Override
        public ResourceLocation getTextureLocation(CyclopsEntity entity) { return TEXTURE; }
    }
}
