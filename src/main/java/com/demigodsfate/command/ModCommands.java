package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Debug/admin commands for the mod.
 * /demigod claim <god> — Force claim a god parent
 * /demigod reset — Reset claiming (Oracle rebirth)
 * /demigod drachma <amount> — Add drachmas
 * /demigod status — Show current demigod status
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("demigod")
                .then(Commands.literal("claim")
                        .then(Commands.argument("god", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    for (GodParent god : GodParent.values()) {
                                        builder.suggest(god.name().toLowerCase());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    if (!(source.getEntity() instanceof Player player)) {
                                        source.sendFailure(Component.literal("Must be a player"));
                                        return 0;
                                    }

                                    String godName = StringArgumentType.getString(context, "god").toUpperCase();
                                    try {
                                        GodParent god = GodParent.valueOf(godName);
                                        GodParentData.claimPlayer(player, god);

                                        // Dramatic claiming message
                                        player.sendSystemMessage(Component.literal("")
                                                .append(Component.literal("⚡ ")
                                                        .withStyle(ChatFormatting.GOLD))
                                                .append(Component.literal("A glowing symbol of " + god.getDisplayName() + " appears above your head!")
                                                        .withStyle(god.getColor(), ChatFormatting.BOLD)));
                                        player.sendSystemMessage(Component.literal("You are a child of " + god.getDisplayName() + "!")
                                                .withStyle(god.getColor()));
                                        player.sendSystemMessage(Component.literal("Camp: " + god.getCamp().getDisplayName())
                                                .withStyle(ChatFormatting.GRAY));

                                        return 1;
                                    } catch (IllegalArgumentException e) {
                                        source.sendFailure(Component.literal("Unknown god: " + godName));
                                        return 0;
                                    }
                                })))
                .then(Commands.literal("reset")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!(source.getEntity() instanceof Player player)) {
                                source.sendFailure(Component.literal("Must be a player"));
                                return 0;
                            }

                            GodParentData.resetClaiming(player);
                            player.sendSystemMessage(Component.literal("The Oracle has reset your claiming. You are unclaimed once more.")
                                    .withStyle(ChatFormatting.LIGHT_PURPLE));
                            return 1;
                        }))
                .then(Commands.literal("drachma")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    if (!(source.getEntity() instanceof Player player)) {
                                        source.sendFailure(Component.literal("Must be a player"));
                                        return 0;
                                    }

                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    GodParentData.addDrachmas(player, amount);
                                    int balance = GodParentData.getDrachmas(player);
                                    player.sendSystemMessage(Component.literal("Added " + amount + " drachmas. Balance: " + balance)
                                            .withStyle(ChatFormatting.GOLD));
                                    return 1;
                                })))
                .then(Commands.literal("status")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!(source.getEntity() instanceof Player player)) {
                                source.sendFailure(Component.literal("Must be a player"));
                                return 0;
                            }

                            boolean claimed = GodParentData.isClaimed(player);
                            GodParent god = GodParentData.getGodParent(player);
                            int drachmas = GodParentData.getDrachmas(player);

                            player.sendSystemMessage(Component.literal("=== Demigod Status ===")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                            if (claimed && god != null) {
                                player.sendSystemMessage(Component.literal("Parent: " + god.getDisplayName())
                                        .withStyle(god.getColor()));
                                player.sendSystemMessage(Component.literal("Camp: " + god.getCamp().getDisplayName())
                                        .withStyle(ChatFormatting.WHITE));
                                player.sendSystemMessage(Component.literal("Domain: " + god.getDomain().name())
                                        .withStyle(ChatFormatting.GRAY));
                            } else {
                                player.sendSystemMessage(Component.literal("Unclaimed")
                                        .withStyle(ChatFormatting.RED));
                            }
                            player.sendSystemMessage(Component.literal("Drachmas: " + drachmas)
                                    .withStyle(ChatFormatting.GOLD));

                            return 1;
                        })));
    }
}
