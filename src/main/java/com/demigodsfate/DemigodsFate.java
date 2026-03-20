package com.demigodsfate;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.item.ModItems;
import com.demigodsfate.block.ModBlocks;
import com.demigodsfate.effect.ModEffects;
import com.demigodsfate.entity.ModEntities;
import com.demigodsfate.network.ModNetwork;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(DemigodsFate.MODID)
public class DemigodsFate {
    public static final String MODID = "demigodsfate";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DEMIGODS_TAB =
            CREATIVE_MODE_TABS.register("demigods_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.demigodsfate"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.GOLDEN_DRACHMA.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Weapons
                        output.accept(ModItems.CELESTIAL_BRONZE_SWORD.get());
                        output.accept(ModItems.CELESTIAL_BRONZE_AXE.get());
                        output.accept(ModItems.CELESTIAL_BRONZE_PICKAXE.get());
                        output.accept(ModItems.CELESTIAL_BRONZE_SHOVEL.get());
                        output.accept(ModItems.CELESTIAL_BRONZE_HOE.get());
                        output.accept(ModItems.IMPERIAL_GOLD_SWORD.get());
                        output.accept(ModItems.IMPERIAL_GOLD_AXE.get());
                        output.accept(ModItems.IMPERIAL_GOLD_PICKAXE.get());
                        output.accept(ModItems.IMPERIAL_GOLD_SHOVEL.get());
                        output.accept(ModItems.IMPERIAL_GOLD_HOE.get());
                        // Divine weapons
                        output.accept(ModItems.RIPTIDE.get());
                        output.accept(ModItems.IVLIVS.get());
                        output.accept(ModItems.AEGIS_SHIELD.get());
                        output.accept(ModItems.BACKBITER.get());
                        // Materials
                        output.accept(ModItems.CELESTIAL_BRONZE_INGOT.get());
                        output.accept(ModItems.IMPERIAL_GOLD_INGOT.get());
                        // Consumables
                        output.accept(ModItems.AMBROSIA.get());
                        output.accept(ModItems.NECTAR.get());
                        output.accept(ModItems.GOLDEN_DRACHMA.get());
                        output.accept(ModItems.GREEK_FIRE.get());
                        // Spawn eggs
                        output.accept(ModItems.MINOTAUR_SPAWN_EGG.get());
                        output.accept(ModItems.HELLHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.FURY_SPAWN_EGG.get());
                        output.accept(ModItems.MEDUSA_SPAWN_EGG.get());
                        output.accept(ModItems.EMPOUSAI_SPAWN_EGG.get());
                        output.accept(ModItems.CYCLOPS_SPAWN_EGG.get());
                    }).build());

    public DemigodsFate(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register all deferred registers
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ITEMS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("Demigod's Fate initializing - Percy Jackson Minecraft Mod");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Demigod's Fate common setup complete");
        LOGGER.info("Available god parents: {}", GodParent.values().length);
    }

    @net.neoforged.bus.api.SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Demigod's Fate server starting - Welcome to the world of Percy Jackson!");
    }
}
