package com.demigodsfate.network;

import com.demigodsfate.DemigodsFate;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Network packet registration for client-server sync.
 * Registers all custom packets using NeoForge's PayloadRegistrar.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(DemigodsFate.MODID).versioned("1.0");

        // Client -> Server: player pressed an ability keybind
        registrar.playToServer(
                UseAbilityPacket.TYPE,
                UseAbilityPacket.STREAM_CODEC,
                UseAbilityPacket::handle
        );
    }
}
