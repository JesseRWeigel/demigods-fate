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
 * Automatically advances quest stages when the player completes objectives.
 * Stage 1 (Arrival): Killing a Minotaur advances to stage 2
 * Stage 4 (Aunty Em's): Killing Medusa advances to stage 5
 * Stage 5 (Gateway Arch): Killing a Chimera advances to stage 6
 * Stage 8 (Showdown): Killing a boss advances to stage 9
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
            player.sendSystemMessage(Component.literal("The Minotaur crumbles to dust!")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("You've proven yourself worthy. Enter the camp.")
                    .withStyle(ChatFormatting.YELLOW));
            QuestManager.advanceStage(player);
            GodParentData.addDrachmas(player, 10);
        }

        // Stage 3 (Aunty Em's) — kill Medusa
        if (stage == 3 && killed.getType() == ModEntities.MEDUSA.get()) {
            player.sendSystemMessage(Component.literal("Medusa's head rolls across the ground...")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            player.sendSystemMessage(Component.literal("You pocket the head — it might be useful later.")
                    .withStyle(ChatFormatting.YELLOW));
            QuestManager.advanceStage(player);
            GodParentData.addDrachmas(player, 15);
        }

        // Stage 4 (Gateway Arch) — kill Chimera
        if (stage == 4 && killed.getType() == ModEntities.CHIMERA.get()) {
            player.sendSystemMessage(Component.literal("The Chimera dissolves into golden dust!")
                    .withStyle(ChatFormatting.GOLD));
            QuestManager.advanceStage(player);
            GodParentData.addDrachmas(player, 15);
        }

        // Stage 5 (Lotus Casino) — just advance manually for now (puzzle, not combat)

        // Stage 7 (Showdown) — kill any boss-tier monster as Ares stand-in
        if (stage == 7 && (killed.getType() == ModEntities.MINOTAUR.get()
                || killed.getType() == ModEntities.HYDRA.get())) {
            player.sendSystemMessage(Component.literal("The god of war falls to his knees!")
                    .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("He tosses you the Master Bolt in disgust.")
                    .withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal("Take it to Zeus on Mount Olympus. /travel olympus")
                    .withStyle(ChatFormatting.AQUA));
            QuestManager.advanceStage(player);
            GodParentData.addDrachmas(player, 25);
        }
    }
}
