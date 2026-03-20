package com.demigodsfate.quest;

import com.demigodsfate.DemigodsFate;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages quest state per player. Tracks active quest and current stage.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class QuestManager {
    private static final String TAG_KEY = "DemigodsFateQuests";
    private static final String TAG_ACTIVE_QUEST = "ActiveQuest";
    private static final String TAG_CURRENT_STAGE = "CurrentStage";

    private static final Map<UUID, QuestState> cache = new HashMap<>();

    @Nullable
    public static String getActiveQuestId(Player player) {
        return getState(player).activeQuestId;
    }

    public static int getCurrentStage(Player player) {
        return getState(player).currentStage;
    }

    /**
     * Start a quest for a player.
     */
    public static void startQuest(Player player, String questId) {
        Quest quest = QuestRegistry.getQuest(questId);
        if (quest == null) return;

        QuestState state = getState(player);
        state.activeQuestId = questId;
        state.currentStage = 0;
        saveState(player, state);

        // Send prophecy
        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.literal("═══ THE ORACLE SPEAKS ═══")
                .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        for (String line : quest.getProphecyLines()) {
            player.sendSystemMessage(Component.literal("  " + line)
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
        }
        player.sendSystemMessage(Component.literal("═════════════════════════")
                .withStyle(ChatFormatting.GREEN));
        player.sendSystemMessage(Component.literal(""));

        // Show first stage
        Quest.QuestStage stage = quest.getStage(0);
        if (stage != null) {
            player.sendSystemMessage(Component.literal("Quest: " + quest.getDisplayName())
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Stage 1: " + stage.getTitle())
                    .withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal("Objective: " + stage.getObjective())
                    .withStyle(ChatFormatting.WHITE));
        }

        DemigodsFate.LOGGER.info("Player {} started quest: {}", player.getName().getString(), questId);
    }

    /**
     * Advance to the next stage of the active quest.
     */
    public static boolean advanceStage(Player player) {
        QuestState state = getState(player);
        if (state.activeQuestId == null) return false;

        Quest quest = QuestRegistry.getQuest(state.activeQuestId);
        if (quest == null) return false;

        state.currentStage++;

        if (state.currentStage >= quest.getStageCount()) {
            // Quest complete!
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("✦ QUEST COMPLETE: " + quest.getDisplayName() + " ✦")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("The prophecy has been fulfilled!")
                    .withStyle(ChatFormatting.YELLOW));

            state.activeQuestId = null;
            state.currentStage = 0;
            saveState(player, state);
            return true;
        }

        Quest.QuestStage stage = quest.getStage(state.currentStage);
        if (stage != null) {
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("Stage " + (state.currentStage + 1) + ": " + stage.getTitle())
                    .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal(stage.getDescription())
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            player.sendSystemMessage(Component.literal("Objective: " + stage.getObjective())
                    .withStyle(ChatFormatting.WHITE));
        }

        saveState(player, state);
        return false;
    }

    /**
     * Show current quest status.
     */
    public static void showStatus(Player player) {
        QuestState state = getState(player);
        if (state.activeQuestId == null) {
            player.sendSystemMessage(Component.literal("No active quest. Visit the Oracle to begin!")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        Quest quest = QuestRegistry.getQuest(state.activeQuestId);
        if (quest == null) return;

        Quest.QuestStage stage = quest.getStage(state.currentStage);

        player.sendSystemMessage(Component.literal("=== Active Quest ===")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal(quest.getDisplayName())
                .withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.literal("Stage " + (state.currentStage + 1) + "/" + quest.getStageCount()
                + ": " + (stage != null ? stage.getTitle() : "???"))
                .withStyle(ChatFormatting.WHITE));
        if (stage != null) {
            player.sendSystemMessage(Component.literal("Objective: " + stage.getObjective())
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    // --- Persistence ---

    private static QuestState getState(Player player) {
        return cache.computeIfAbsent(player.getUUID(), uuid -> loadState(player));
    }

    private static QuestState loadState(Player player) {
        CompoundTag persistent = player.getPersistentData();
        QuestState state = new QuestState();
        if (persistent.contains(TAG_KEY)) {
            CompoundTag tag = persistent.getCompound(TAG_KEY);
            state.activeQuestId = tag.contains(TAG_ACTIVE_QUEST) ? tag.getString(TAG_ACTIVE_QUEST) : null;
            state.currentStage = tag.getInt(TAG_CURRENT_STAGE);
        }
        return state;
    }

    private static void saveState(Player player, QuestState state) {
        CompoundTag tag = new CompoundTag();
        if (state.activeQuestId != null) {
            tag.putString(TAG_ACTIVE_QUEST, state.activeQuestId);
        }
        tag.putInt(TAG_CURRENT_STAGE, state.currentStage);
        player.getPersistentData().put(TAG_KEY, tag);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        cache.put(event.getEntity().getUUID(), loadState(event.getEntity()));
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        cache.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        CompoundTag original = event.getOriginal().getPersistentData();
        if (original.contains(TAG_KEY)) {
            event.getEntity().getPersistentData().put(TAG_KEY, original.getCompound(TAG_KEY).copy());
            cache.put(event.getEntity().getUUID(), loadState(event.getEntity()));
        }
    }

    private static class QuestState {
        @Nullable String activeQuestId = null;
        int currentStage = 0;
    }
}
