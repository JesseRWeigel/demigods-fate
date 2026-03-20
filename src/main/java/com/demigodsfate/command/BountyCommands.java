package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.quest.BountySystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * /bounty list — Show available bounties
 * /bounty accept <number> — Accept a bounty
 * /bounty status — Show active bounty progress
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class BountyCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("bounty")
                .then(Commands.literal("list")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof Player player)) return 0;

                            player.sendSystemMessage(Component.literal("=== Monster Bounties ===")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                            var bounties = BountySystem.getAvailableBounties();
                            for (int i = 0; i < bounties.size(); i++) {
                                var b = bounties.get(i);
                                player.sendSystemMessage(Component.literal(
                                        "  [" + (i + 1) + "] " + b.name() + " — Kill " + b.count() + " — " + b.reward() + " drachmas")
                                        .withStyle(ChatFormatting.YELLOW));
                            }
                            player.sendSystemMessage(Component.literal("Use /bounty accept <number> to start")
                                    .withStyle(ChatFormatting.GRAY));
                            return 1;
                        }))
                .then(Commands.literal("accept")
                        .then(Commands.argument("number", IntegerArgumentType.integer(1, 6))
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof Player player)) return 0;
                                    int num = IntegerArgumentType.getInteger(context, "number") - 1;
                                    BountySystem.acceptBounty(player, num);
                                    return 1;
                                })))
                .then(Commands.literal("status")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof Player player)) return 0;
                            BountySystem.showActiveBounty(player);
                            return 1;
                        })));
    }
}
