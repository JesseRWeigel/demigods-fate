package com.demigodsfate.network;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.ability.AbilityManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent from client to server when a player presses an ability keybind.
 * Contains the ability slot index (0=R, 1=V, 2=G).
 */
public record UseAbilityPacket(int slot) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UseAbilityPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "use_ability"));

    public static final StreamCodec<ByteBuf, UseAbilityPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, UseAbilityPacket::slot,
                    UseAbilityPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Server-side handler. Validates the slot and delegates to AbilityManager.
     */
    public static void handle(UseAbilityPacket packet, IPayloadContext context) {
        // Validate slot range
        if (packet.slot() < 0 || packet.slot() > 2) {
            return;
        }
        // enqueueWork ensures we run on the main server thread
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                AbilityManager.tryUseAbility(serverPlayer, packet.slot());
            }
        });
    }
}
