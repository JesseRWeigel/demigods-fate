package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Oracle commands for rebirth (respec) and prophecy delivery.
 * /oracle rebirth <god> — Reset parentage and reclaim (costs 50 drachmas)
 * /oracle prophecy — Receive a random prophecy hint
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class OracleCommands {
    private static final int REBIRTH_COST = 50;

    private static final String[] PROPHECIES = {
            "The son of the sea god shall walk the earth...",
            "A child of lightning faces the darkness alone...",
            "Wisdom's daughter walks alone, the mark burns through the bone...",
            "The forge master's flame shall light the way through shadow...",
            "Seven half-bloods shall answer the call...",
            "To storm or fire, the world must fall...",
            "An oath to keep with a final breath...",
            "Beware the earth mother's waking wrath...",
            "The Oracle speaks in riddles — listen carefully...",
            "Your path leads west. Trust in your abilities.",
    };

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("oracle")
                .then(Commands.literal("rebirth")
                        .then(Commands.argument("newgod", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    for (GodParent god : GodParent.values()) {
                                        builder.suggest(god.name().toLowerCase());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

                                    if (!GodParentData.isClaimed(player)) {
                                        player.sendSystemMessage(Component.literal("You must be claimed first before seeking rebirth.")
                                                .withStyle(ChatFormatting.RED));
                                        return 0;
                                    }

                                    int drachmas = GodParentData.getDrachmas(player);
                                    if (drachmas < REBIRTH_COST) {
                                        player.sendSystemMessage(Component.literal("The Oracle requires " + REBIRTH_COST + " drachmas for rebirth. You have " + drachmas + ".")
                                                .withStyle(ChatFormatting.RED));
                                        return 0;
                                    }

                                    String godName = StringArgumentType.getString(context, "newgod").toUpperCase();
                                    GodParent newGod;
                                    try {
                                        newGod = GodParent.valueOf(godName);
                                    } catch (IllegalArgumentException e) {
                                        player.sendSystemMessage(Component.literal("Unknown god: " + godName)
                                                .withStyle(ChatFormatting.RED));
                                        return 0;
                                    }

                                    GodParent oldGod = GodParentData.getGodParent(player);

                                    // Perform rebirth
                                    GodParentData.spendDrachmas(player, REBIRTH_COST);
                                    GodParentData.resetClaiming(player);
                                    GodParentData.claimPlayer(player, newGod);

                                    // Dramatic rebirth sequence
                                    player.sendSystemMessage(Component.literal(""));
                                    player.sendSystemMessage(Component.literal("═══ THE ORACLE'S REBIRTH ═══")
                                            .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                                    player.sendSystemMessage(Component.literal("Green mist swirls around you...")
                                            .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
                                    if (oldGod != null) {
                                        player.sendSystemMessage(Component.literal("The mark of " + oldGod.getDisplayName() + " fades from above your head...")
                                                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                                    }
                                    player.sendSystemMessage(Component.literal("A new symbol blazes to life — " + newGod.getDisplayName() + "!")
                                            .withStyle(newGod.getColor(), ChatFormatting.BOLD));
                                    player.sendSystemMessage(Component.literal("You are reborn as a child of " + newGod.getDisplayName() + "!")
                                            .withStyle(newGod.getColor()));
                                    player.sendSystemMessage(Component.literal("(-" + REBIRTH_COST + " drachmas)")
                                            .withStyle(ChatFormatting.GOLD));
                                    player.sendSystemMessage(Component.literal("═════════════════════════════")
                                            .withStyle(ChatFormatting.LIGHT_PURPLE));

                                    // Particles
                                    if (player.level() instanceof ServerLevel sl) {
                                        sl.sendParticles(ParticleTypes.WITCH,
                                                player.getX(), player.getY() + 2, player.getZ(),
                                                50, 1.0, 1.5, 1.0, 0.1);
                                        sl.sendParticles(ParticleTypes.END_ROD,
                                                player.getX(), player.getY() + 2.5, player.getZ(),
                                                30, 0.5, 1.0, 0.5, 0.05);
                                    }

                                    return 1;
                                })))
                .then(Commands.literal("prophecy")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

                            String prophecy = PROPHECIES[player.getRandom().nextInt(PROPHECIES.length)];

                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal("The Oracle speaks:")
                                    .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
                            player.sendSystemMessage(Component.literal("  \"" + prophecy + "\"")
                                    .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
                            player.sendSystemMessage(Component.literal(""));

                            if (player.level() instanceof ServerLevel sl) {
                                sl.sendParticles(ParticleTypes.WITCH,
                                        player.getX(), player.getY() + 1.5, player.getZ(),
                                        20, 0.5, 0.5, 0.5, 0.05);
                            }

                            return 1;
                        })));
    }
}
