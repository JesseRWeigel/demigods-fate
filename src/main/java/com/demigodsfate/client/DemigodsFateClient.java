package com.demigodsfate.client;

import com.demigodsfate.DemigodsFate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * Client-side entry point. Registers keybinds, HUD overlays, and renderers.
 */
@Mod(value = DemigodsFate.MODID, dist = Dist.CLIENT)
public class DemigodsFateClient {
    public DemigodsFateClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(AbilityKeyBinds::registerKeyMappings);
        DemigodsFate.LOGGER.info("Demigod's Fate client initialized");
    }
}
