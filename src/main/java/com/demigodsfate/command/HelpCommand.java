package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * /demigodhelp — Shows a comprehensive guide for new players.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class HelpCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("demigodhelp")
                .executes(context -> {
                    if (!(context.getSource().getEntity() instanceof Player player)) return 0;

                    player.sendSystemMessage(Component.literal(""));
                    player.sendSystemMessage(Component.literal("═══ DEMIGOD'S FATE — GUIDE ═══")
                            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                    player.sendSystemMessage(Component.literal(""));

                    player.sendSystemMessage(Component.literal("Getting Started:")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
                    player.sendSystemMessage(Component.literal("  /demigod claim <god> — Choose your godly parent")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal("  /demigod abilities — See your 3 powers")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal("  Press R, V, G — Use abilities in combat")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal(""));

                    player.sendSystemMessage(Component.literal("Greek Gods: ")
                            .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD)
                            .append(Component.literal("poseidon, zeus, athena, ares, hephaestus, apollo, hermes")
                                    .withStyle(ChatFormatting.WHITE)));
                    player.sendSystemMessage(Component.literal("Roman Gods: ")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
                            .append(Component.literal("jupiter, neptune, mars, pluto, minerva, bellona")
                                    .withStyle(ChatFormatting.WHITE)));
                    player.sendSystemMessage(Component.literal(""));

                    player.sendSystemMessage(Component.literal("Camps & Quests:")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
                    player.sendSystemMessage(Component.literal("  /build camp_halfblood — Build Greek camp")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal("  /build camp_jupiter — Build Roman camp")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal("  /quest start the_lightning_thief — Main quest")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal("  /bounty list — Monster hunt side quests")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal(""));

                    player.sendSystemMessage(Component.literal("Dimensions:")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
                    player.sendSystemMessage(Component.literal("  /travel underworld — Enter the Underworld")
                            .withStyle(ChatFormatting.DARK_GRAY));
                    player.sendSystemMessage(Component.literal("  /travel olympus — Ascend to Mount Olympus")
                            .withStyle(ChatFormatting.GOLD));
                    player.sendSystemMessage(Component.literal("  /travel labyrinth — Procedural maze dungeon")
                            .withStyle(ChatFormatting.DARK_RED));
                    player.sendSystemMessage(Component.literal("  /travel overworld — Return home")
                            .withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(Component.literal(""));

                    player.sendSystemMessage(Component.literal("Other Commands:")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
                    player.sendSystemMessage(Component.literal("  /iris <god/chiron> — Iris Message (1 drachma)")
                            .withStyle(ChatFormatting.LIGHT_PURPLE));
                    player.sendSystemMessage(Component.literal("  /oracle prophecy — Hear a prophecy")
                            .withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(Component.literal("  /oracle rebirth <god> — Switch gods (50 drachmas)")
                            .withStyle(ChatFormatting.LIGHT_PURPLE));
                    player.sendSystemMessage(Component.literal("  /demigod status — Check your stats")
                            .withStyle(ChatFormatting.WHITE));
                    player.sendSystemMessage(Component.literal(""));
                    player.sendSystemMessage(Component.literal("═══════════════════════════════")
                            .withStyle(ChatFormatting.GOLD));

                    return 1;
                }));
    }
}
