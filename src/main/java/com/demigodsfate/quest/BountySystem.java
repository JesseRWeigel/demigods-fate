package com.demigodsfate.quest;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.ModEntities;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.*;

/**
 * Monster bounty system — repeatable side quests.
 * Players accept bounties to kill specific monsters for drachma rewards.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class BountySystem {

    public record Bounty(String name, java.util.function.Supplier<EntityType<?>> targetType, int count, int reward) {}

    // Use Suppliers to avoid accessing deferred registry values at class init time
    private static final List<Bounty> AVAILABLE_BOUNTIES = List.of(
            new Bounty("Hellhound Hunt", () -> ModEntities.HELLHOUND.get(), 3, 15),
            new Bounty("Fury Patrol", () -> ModEntities.FURY.get(), 2, 20),
            new Bounty("Cyclops Slayer", () -> ModEntities.CYCLOPS.get(), 1, 25),
            new Bounty("Empousai Extermination", () -> ModEntities.EMPOUSAI.get(), 3, 18),
            new Bounty("Chimera Challenge", () -> ModEntities.CHIMERA.get(), 1, 30),
            new Bounty("Minotaur Rematch", () -> ModEntities.MINOTAUR.get(), 1, 35)
    );

    // Player UUID -> active bounty index, kills remaining
    private static final Map<UUID, ActiveBounty> activeBounties = new HashMap<>();

    public static List<Bounty> getAvailableBounties() { return AVAILABLE_BOUNTIES; }

    public static void acceptBounty(Player player, int index) {
        if (index < 0 || index >= AVAILABLE_BOUNTIES.size()) return;

        Bounty bounty = AVAILABLE_BOUNTIES.get(index);
        activeBounties.put(player.getUUID(), new ActiveBounty(index, bounty.count));

        player.sendSystemMessage(Component.literal("Bounty accepted: " + bounty.name)
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal("Kill " + bounty.count + " " + bounty.name.split(" ")[0] + "(s)")
                .withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.literal("Reward: " + bounty.reward + " drachmas")
                .withStyle(ChatFormatting.GOLD));
    }

    public static void showActiveBounty(Player player) {
        ActiveBounty active = activeBounties.get(player.getUUID());
        if (active == null) {
            player.sendSystemMessage(Component.literal("No active bounty. Use /bounty list to see available ones.")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        Bounty bounty = AVAILABLE_BOUNTIES.get(active.bountyIndex);
        player.sendSystemMessage(Component.literal("=== Active Bounty ===")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal(bounty.name + " — " + active.killsRemaining + " kills remaining")
                .withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.literal("Reward: " + bounty.reward + " drachmas")
                .withStyle(ChatFormatting.GOLD));
    }

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        LivingEntity killed = event.getEntity();
        if (killed.level().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ActiveBounty active = activeBounties.get(player.getUUID());
        if (active == null) return;

        Bounty bounty = AVAILABLE_BOUNTIES.get(active.bountyIndex);
        if (killed.getType() == bounty.targetType.get()) {
            active.killsRemaining--;

            if (active.killsRemaining <= 0) {
                // Bounty complete!
                GodParentData.addDrachmas(player, bounty.reward);
                activeBounties.remove(player.getUUID());

                player.sendSystemMessage(Component.literal(""));
                player.sendSystemMessage(Component.literal("Bounty Complete: " + bounty.name + "!")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                player.sendSystemMessage(Component.literal("+" + bounty.reward + " drachmas!")
                        .withStyle(ChatFormatting.GOLD));
            } else {
                player.sendSystemMessage(Component.literal("Bounty progress: " + active.killsRemaining + " kills remaining")
                        .withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    private static class ActiveBounty {
        int bountyIndex;
        int killsRemaining;

        ActiveBounty(int bountyIndex, int killsRemaining) {
            this.bountyIndex = bountyIndex;
            this.killsRemaining = killsRemaining;
        }
    }
}
