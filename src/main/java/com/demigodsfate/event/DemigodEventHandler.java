package com.demigodsfate.event;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * General event handler for demigod-related events.
 * Handles first-time player messages and camp discovery hints.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class DemigodEventHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        // Welcome message for new players
        if (!GodParentData.isClaimed(player)) {
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("═══════════════════════════════════")
                    .withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal("  Welcome to Demigod's Fate!")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("  A Percy Jackson Minecraft Mod")
                    .withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal("═══════════════════════════════════")
                    .withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("You are an unclaimed demigod.")
                    .withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Use /demigod claim <god> to be claimed.")
                    .withStyle(ChatFormatting.AQUA));
            player.sendSystemMessage(Component.literal("Available gods: poseidon, zeus, athena, ares,")
                    .withStyle(ChatFormatting.WHITE));
            player.sendSystemMessage(Component.literal("  hephaestus, apollo, hermes (Greek)")
                    .withStyle(ChatFormatting.WHITE));
            player.sendSystemMessage(Component.literal("  jupiter, neptune, mars, pluto, minerva, bellona (Roman)")
                    .withStyle(ChatFormatting.WHITE));
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("Then use /demigod abilities to see your powers!")
                    .withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.literal("Start a quest with /quest start the_lightning_thief")
                    .withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.literal(""));
        } else {
            GodParent god = GodParentData.getGodParent(player);
            if (god != null) {
                player.sendSystemMessage(Component.literal("Welcome back, child of " + god.getDisplayName() + "!")
                        .withStyle(god.getColor()));
                int drachmas = GodParentData.getDrachmas(player);
                player.sendSystemMessage(Component.literal("You have " + drachmas + " drachmas.")
                        .withStyle(ChatFormatting.GOLD));
            }
        }
    }
}
