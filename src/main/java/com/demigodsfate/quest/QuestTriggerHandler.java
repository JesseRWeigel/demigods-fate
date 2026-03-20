package com.demigodsfate.quest;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.ModEntities;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Automatically advances quest stages when objectives are completed.
 * Also handles auto-skipping non-combat stages.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class QuestTriggerHandler {

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        LivingEntity killed = event.getEntity();
        if (killed.level().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        String questId = QuestManager.getActiveQuestId(player);
        if (!"the_lightning_thief".equals(questId)) return;

        int stage = QuestManager.getCurrentStage(player);

        // Stage 0 (Arrival) — kill Minotaur to advance
        if (stage == 0 && killed.getType() == ModEntities.MINOTAUR.get()) {
            player.sendSystemMessage(Component.literal("")
                    .withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal("The Minotaur crumbles to golden dust!")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("You've proven yourself worthy!")
                    .withStyle(ChatFormatting.YELLOW));
            GodParentData.addDrachmas(player, 10);

            // Auto-advance through stage 0 (Arrival) → 1 (Training) → 2 (Prophecy) → 3 (Aunty Em's)
            // Stages 1 and 2 are non-combat, so we skip them with flavor text
            QuestManager.advanceStage(player); // → Stage 1: Camp Training

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("You train at camp, learning to fight with celestial bronze...")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            QuestManager.advanceStage(player); // → Stage 2: Receive Prophecy

            player.sendSystemMessage(Component.literal("The Oracle speaks your prophecy...")
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));

            // Show the prophecy
            Quest quest = QuestRegistry.getQuest("the_lightning_thief");
            if (quest != null) {
                for (String line : quest.getProphecyLines()) {
                    player.sendSystemMessage(Component.literal("  " + line)
                            .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
                }
            }

            QuestManager.advanceStage(player); // → Stage 3: Aunty Em's Garden

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("Your quest begins! Find and defeat Medusa.")
                    .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Tip: Don't look at her! Use a shield to reflect the gaze.")
                    .withStyle(ChatFormatting.AQUA));
        }

        // Stage 3 (Aunty Em's) — kill Medusa
        if (stage == 3 && killed.getType() == ModEntities.MEDUSA.get()) {
            player.sendSystemMessage(Component.literal("Medusa's head rolls across the ground...")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            player.sendSystemMessage(Component.literal("You pocket the head — it might be useful later.")
                    .withStyle(ChatFormatting.YELLOW));
            GodParentData.addDrachmas(player, 15);
            QuestManager.advanceStage(player); // → Stage 4: Gateway Arch

            player.sendSystemMessage(Component.literal("Next: Survive the Chimera at the Gateway Arch!")
                    .withStyle(ChatFormatting.YELLOW));
        }

        // Stage 4 (Gateway Arch) — kill Chimera
        if (stage == 4 && killed.getType() == ModEntities.CHIMERA.get()) {
            player.sendSystemMessage(Component.literal("The Chimera dissolves into golden dust!")
                    .withStyle(ChatFormatting.GOLD));
            GodParentData.addDrachmas(player, 15);
            QuestManager.advanceStage(player); // → Stage 5: Lotus Casino

            // Auto-skip Lotus Casino (stage 5) and Underworld (stage 6) for now
            player.sendSystemMessage(Component.literal("You escape the Lotus Casino just in time...")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            QuestManager.advanceStage(player); // → Stage 6: Underworld

            player.sendSystemMessage(Component.literal("You descend into the Underworld and confront Hades...")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            player.sendSystemMessage(Component.literal("Hades reveals the truth — Ares has the bolt!")
                    .withStyle(ChatFormatting.RED));
            QuestManager.advanceStage(player); // → Stage 7: Showdown

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("FINAL BATTLE: Defeat a powerful monster to reclaim the Master Bolt!")
                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Summon a Hydra or Minotaur for the final showdown!")
                    .withStyle(ChatFormatting.YELLOW));
        }

        // Stage 7 (Showdown) — kill any boss
        if (stage == 7 && (killed.getType() == ModEntities.MINOTAUR.get()
                || killed.getType() == ModEntities.HYDRA.get()
                || killed.getType() == ModEntities.CHIMERA.get()
                || killed.getType() == ModEntities.CYCLOPS.get())) {
            player.sendSystemMessage(Component.literal("You've reclaimed Zeus's Master Bolt!")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Use /travel olympus to deliver it to Zeus!")
                    .withStyle(ChatFormatting.AQUA));
            GodParentData.addDrachmas(player, 25);
            QuestManager.advanceStage(player); // → Stage 8: Mount Olympus

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("Final step: /travel olympus then /quest advance to complete!")
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        }
    }
}
