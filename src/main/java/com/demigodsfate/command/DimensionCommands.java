package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Commands for traveling between dimensions.
 * /travel underworld — Enter the Underworld
 * /travel olympus — Travel to Mount Olympus (future)
 * /travel overworld — Return to the Overworld
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class DimensionCommands {

    public static final ResourceKey<Level> UNDERWORLD_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "underworld"));

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("travel")
                .then(Commands.literal("underworld")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

                            ServerLevel underworld = player.server.getLevel(UNDERWORLD_KEY);
                            if (underworld == null) {
                                player.sendSystemMessage(Component.literal("The Underworld is not accessible...")
                                        .withStyle(ChatFormatting.RED));
                                return 0;
                            }

                            player.sendSystemMessage(Component.literal("")
                                    .withStyle(ChatFormatting.DARK_GRAY));
                            player.sendSystemMessage(Component.literal("You descend into the depths...")
                                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                            player.sendSystemMessage(Component.literal("The River Styx churns below.")
                                    .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC));
                            player.sendSystemMessage(Component.literal("Welcome to the Underworld.")
                                    .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));

                            BlockPos spawnPos = new BlockPos(0, 64, 0);
                            player.teleportTo(underworld, spawnPos.getX() + 0.5, spawnPos.getY(),
                                    spawnPos.getZ() + 0.5, player.getYRot(), player.getXRot());
                            return 1;
                        }))
                .then(Commands.literal("overworld")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

                            ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);
                            if (overworld == null) return 0;

                            player.sendSystemMessage(Component.literal("You return to the mortal world...")
                                    .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));

                            BlockPos spawnPos = overworld.getSharedSpawnPos();
                            player.teleportTo(overworld, spawnPos.getX() + 0.5, spawnPos.getY(),
                                    spawnPos.getZ() + 0.5, player.getYRot(), player.getXRot());
                            return 1;
                        })));
    }
}
