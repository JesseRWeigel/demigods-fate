package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Map;
import java.util.Random;

/**
 * Iris Message system — call the gods or camp leaders for advice.
 * Costs 1 golden drachma per message.
 * /iris <recipient> — Send an Iris message
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class IrisMessageCommand {

    private static final Map<String, String[]> RESPONSES = Map.of(
            "chiron", new String[]{
                    "Stay vigilant, young hero. The monsters grow bolder each day.",
                    "Remember your training. A demigod's greatest weapon is their mind.",
                    "The Oracle's words always come true... but never in the way you expect.",
                    "Camp Half-Blood stands behind you. We believe in you."
            },
            "poseidon", new String[]{
                    "The sea does not like to be restrained, child. Neither should you.",
                    "I am watching over you. The waters will answer your call.",
                    "Be wary of my brother's wrath. Zeus does not forgive easily.",
                    "Your mother would be proud of you."
            },
            "zeus", new String[]{
                    "Do not disappoint me, demigod. The sky sees all.",
                    "My bolt must be returned. Failure is not an option.",
                    "The winds carry whispers of war. Be prepared.",
                    "Lightning does not strike the same place twice... unless I will it."
            },
            "athena", new String[]{
                    "Strategy, child. Think before you act.",
                    "Every labyrinth has a solution. Look for the pattern.",
                    "Knowledge is the sharpest blade. Use it wisely.",
                    "My owl watches over you. You are never truly alone."
            },
            "ares", new String[]{
                    "FIGHT! What are you waiting for?!",
                    "You want glory? You have to TAKE it!",
                    "War is coming, kid. Pick a side.",
                    "Not bad... for a runt. Keep killing."
            },
            "apollo", new String[]{
                    "Here's a haiku: The quest lies ahead / Golden drachmas light the way / Trust in the sun's warmth",
                    "I'd write you a poem, but I'm too busy being awesome.",
                    "The Oracle speaks my truth. Listen carefully to her words.",
                    "Stay in the light, young hero. Darkness hides many dangers."
            },
            "hermes", new String[]{
                    "I deliver messages, not miracles. But I'll try.",
                    "Watch your pockets around my kids. Just saying.",
                    "The road is long, but every journey starts with a single step... or a stolen car.",
                    "Need anything delivered? Express shipping is extra."
            }
    );

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("iris")
                .then(Commands.argument("recipient", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            RESPONSES.keySet().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof Player player)) return 0;

                            String recipient = StringArgumentType.getString(context, "recipient").toLowerCase();

                            // Check drachma cost
                            if (!GodParentData.spendDrachmas(player, 1)) {
                                player.sendSystemMessage(Component.literal("You need 1 golden drachma for an Iris message!")
                                        .withStyle(ChatFormatting.RED));
                                return 0;
                            }

                            String[] responses = RESPONSES.get(recipient);
                            if (responses == null) {
                                player.sendSystemMessage(Component.literal("The rainbow shimmers but no one answers...")
                                        .withStyle(ChatFormatting.GRAY));
                                GodParentData.addDrachmas(player, 1); // Refund
                                return 0;
                            }

                            String response = responses[new Random().nextInt(responses.length)];

                            // Display the Iris message
                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal("A rainbow shimmers in the mist... (-1 drachma)")
                                    .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
                            player.sendSystemMessage(Component.literal("[" + capitalize(recipient) + "] ")
                                    .withStyle(ChatFormatting.GOLD)
                                    .append(Component.literal(response)
                                            .withStyle(ChatFormatting.WHITE)));
                            player.sendSystemMessage(Component.literal("The image fades...")
                                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                            player.sendSystemMessage(Component.literal(""));

                            return 1;
                        })));
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
