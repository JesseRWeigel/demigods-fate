package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.world.structure.CampHalfBloodBuilder;
import com.demigodsfate.world.structure.CampJupiterBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Commands for building camp structures.
 * /build camp_halfblood — Generates Camp Half-Blood at the player's current position
 * /build camp_jupiter — Generates Camp Jupiter at the player's current position
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class BuildCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("build")
                .requires(source -> source.hasPermission(2)) // Require OP level 2
                .then(Commands.literal("camp_halfblood")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!(source.getEntity() instanceof ServerPlayer player)) {
                                source.sendFailure(Component.literal("Must be a player"));
                                return 0;
                            }

                            ServerLevel level = player.serverLevel();
                            BlockPos pos = player.blockPosition();

                            player.sendSystemMessage(Component.literal("Building Camp Half-Blood... This may take a moment.")
                                    .withStyle(ChatFormatting.GOLD));

                            CampHalfBloodBuilder.buildCamp(level, pos);

                            player.sendSystemMessage(Component.literal("")
                                    .append(Component.literal("Camp Half-Blood has been built!")
                                            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
                            player.sendSystemMessage(Component.literal("  12 cabins, Big House, and campfire generated.")
                                    .withStyle(ChatFormatting.GRAY));
                            player.sendSystemMessage(Component.literal("  Chiron and Mr. D have been summoned.")
                                    .withStyle(ChatFormatting.GRAY));
                            player.sendSystemMessage(Component.literal("  Safe zone registered (radius 80).")
                                    .withStyle(ChatFormatting.YELLOW));

                            return 1;
                        }))
                .then(Commands.literal("camp_jupiter")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!(source.getEntity() instanceof ServerPlayer player)) {
                                source.sendFailure(Component.literal("Must be a player"));
                                return 0;
                            }

                            ServerLevel level = player.serverLevel();
                            BlockPos pos = player.blockPosition();

                            player.sendSystemMessage(Component.literal("Building Camp Jupiter... This may take a moment.")
                                    .withStyle(ChatFormatting.GOLD));

                            CampJupiterBuilder.buildCamp(level, pos);

                            player.sendSystemMessage(Component.literal("")
                                    .append(Component.literal("Camp Jupiter has been built!")
                                            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
                            player.sendSystemMessage(Component.literal("  5 cohort barracks, Principia, Senate House, and temples generated.")
                                    .withStyle(ChatFormatting.GRAY));
                            player.sendSystemMessage(Component.literal("  Little Tiber river flows to the south.")
                                    .withStyle(ChatFormatting.GRAY));
                            player.sendSystemMessage(Component.literal("  Safe zone registered (radius 90).")
                                    .withStyle(ChatFormatting.YELLOW));

                            return 1;
                        })));
    }
}
