package com.demigodsfate.quest;

import java.util.*;

/**
 * Registry of all quests in the mod.
 */
public class QuestRegistry {
    private static final Map<String, Quest> QUESTS = new LinkedHashMap<>();

    static {
        registerLightningThief();
    }

    private static void registerLightningThief() {
        List<Quest.QuestStage> stages = List.of(
                new Quest.QuestStage("arrival", "Arrival",
                        "A monster attacks at the camp border. Defeat it to enter camp.",
                        "Defeat the Minotaur"),
                new Quest.QuestStage("camp_training", "Camp Training",
                        "Learn the ways of the demigod. Complete training exercises at camp.",
                        "Complete 3 training exercises"),
                new Quest.QuestStage("receive_prophecy", "The Prophecy",
                        "Visit the Oracle to receive your quest prophecy.",
                        "Speak to the Oracle"),
                new Quest.QuestStage("aunty_em", "Aunty Em's Garden",
                        "A statue garden hides a deadly secret. Investigate but don't look!",
                        "Defeat Medusa"),
                new Quest.QuestStage("gateway_arch", "The Gateway Arch",
                        "Ambushed at a great arch by the Mother of Monsters.",
                        "Survive the Chimera attack"),
                new Quest.QuestStage("lotus_casino", "The Lotus Casino",
                        "A casino where time stands still. Don't eat the flowers!",
                        "Escape the Lotus Casino"),
                new Quest.QuestStage("underworld", "The Underworld",
                        "Enter the land of the dead to confront the Lord of the Dead.",
                        "Find Hades and the Master Bolt"),
                new Quest.QuestStage("showdown", "The Showdown",
                        "A god of war waits on the beach. He has what you seek.",
                        "Defeat Ares on the beach"),
                new Quest.QuestStage("olympus", "Mount Olympus",
                        "Return the Master Bolt to Zeus on Mount Olympus.",
                        "Deliver the bolt to Zeus")
        );

        Quest lightningThief = new Quest(
                "the_lightning_thief",
                "The Lightning Thief",
                new String[]{
                        "You shall go west, and face the god who has turned,",
                        "You shall find what was stolen, and see it safely returned,",
                        "You shall be betrayed by one who calls you a friend,",
                        "And you shall fail to save what matters most, in the end."
                },
                stages
        );

        QUESTS.put(lightningThief.getId(), lightningThief);
    }

    public static Quest getQuest(String id) {
        return QUESTS.get(id);
    }

    public static Collection<Quest> getAllQuests() {
        return QUESTS.values();
    }
}
