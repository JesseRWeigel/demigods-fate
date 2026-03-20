package com.demigodsfate.entity.npc;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.quest.QuestManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base NPC entity for camp characters.
 * NPCs are immortal, can't be pushed, and have dialogue when interacted with.
 */
public class CampNpcEntity extends PathfinderMob {
    private final String npcName;
    private final String[] dialogueLines;
    private int dialogueIndex = 0;

    public CampNpcEntity(EntityType<? extends PathfinderMob> type, Level level,
                         String npcName, String... dialogueLines) {
        super(type, level);
        this.npcName = npcName;
        this.dialogueLines = dialogueLines;
        this.setInvulnerable(true);
        this.setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.4));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.level().isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.SUCCESS;
        }

        // Cycle through dialogue
        if (dialogueLines != null && dialogueLines.length > 0) {
            String line = dialogueLines[dialogueIndex % dialogueLines.length];
            player.sendSystemMessage(Component.literal("[" + npcName + "] ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(line)
                            .withStyle(ChatFormatting.WHITE)));
            dialogueIndex++;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isPushable() { return false; }

    @Override
    public boolean canBeLeashed() { return false; }

    public String getNpcName() { return npcName; }
}
