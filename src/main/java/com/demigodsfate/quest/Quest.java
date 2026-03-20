package com.demigodsfate.quest;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Represents a quest in the Percy Jackson mod.
 * Quests are prophecy-driven: the Oracle delivers a cryptic prophecy,
 * and the player follows it to complete stages.
 */
public class Quest {
    private final String id;
    private final String displayName;
    private final String[] prophecyLines;
    private final List<QuestStage> stages;

    public Quest(String id, String displayName, String[] prophecyLines, List<QuestStage> stages) {
        this.id = id;
        this.displayName = displayName;
        this.prophecyLines = prophecyLines;
        this.stages = stages;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String[] getProphecyLines() { return prophecyLines; }
    public List<QuestStage> getStages() { return stages; }

    public QuestStage getStage(int index) {
        if (index >= 0 && index < stages.size()) return stages.get(index);
        return null;
    }

    public int getStageCount() { return stages.size(); }

    /**
     * A single stage within a quest.
     */
    public static class QuestStage {
        private final String id;
        private final String title;
        private final String description;
        private final String objective;

        public QuestStage(String id, String title, String description, String objective) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.objective = objective;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getObjective() { return objective; }
    }
}
