package com.demigodsfate.entity.npc;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.quest.Quest;
import com.demigodsfate.quest.QuestManager;
import com.demigodsfate.quest.QuestRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Oracle of Delphi — the mummy in the attic of the Big House.
 * Right-click to:
 * 1. Start the Lightning Thief quest (if no active quest)
 * 2. Get quest status (if quest active)
 * 3. Get a prophecy hint
 */
public class OracleEntity extends CampNpcEntity {
    private static final String[] PROPHECY_HINTS = {
            "I see darkness in your future... and light beyond it.",
            "The son of the sea god walks among us...",
            "Beware the one who calls you friend...",
            "The Master Bolt must be returned... or all is lost.",
            "Seven shall stand against the Earth Mother...",
            "Fire and water — the choice is yours alone.",
            "The Oracle sees all... but tells only in riddles.",
    };

    public OracleEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, "The Oracle",
                "Green mist swirls around the ancient figure...");
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.level().isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.SUCCESS;
        }

        // Green mist particles
        if (player.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.WITCH,
                    getX(), getY() + 1.5, getZ(),
                    30, 0.5, 1.0, 0.5, 0.05);
        }

        String activeQuest = QuestManager.getActiveQuestId(player);

        if (!GodParentData.isClaimed(player)) {
            // Not claimed yet
            player.sendSystemMessage(Component.literal("[The Oracle] ")
                    .withStyle(ChatFormatting.GREEN)
                    .append(Component.literal("The mist swirls but reveals nothing... You must be claimed first.")
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
            player.sendSystemMessage(Component.literal("  Use /demigod claim <god> to discover your parentage.")
                    .withStyle(ChatFormatting.AQUA));
            return InteractionResult.SUCCESS;
        }

        if (activeQuest == null) {
            // No quest — start the Lightning Thief!
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("[The Oracle] ")
                    .withStyle(ChatFormatting.GREEN)
                    .append(Component.literal("The Oracle's eyes glow green. Mist pours from her mouth...")
                            .withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.ITALIC)));
            player.sendSystemMessage(Component.literal(""));

            QuestManager.startQuest(player, "the_lightning_thief");

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("Your quest has begun! Defeat the Minotaur to prove yourself!")
                    .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
            return InteractionResult.SUCCESS;
        }

        // Quest active — show status and a hint
        QuestManager.showStatus(player);
        player.sendSystemMessage(Component.literal(""));

        String hint = PROPHECY_HINTS[player.getRandom().nextInt(PROPHECY_HINTS.length)];
        player.sendSystemMessage(Component.literal("[The Oracle] ")
                .withStyle(ChatFormatting.GREEN)
                .append(Component.literal("\"" + hint + "\"")
                        .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC)));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        // Ambient green particles
        if (!level().isClientSide() && tickCount % 40 == 0 && level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.WITCH,
                    getX(), getY() + 1, getZ(),
                    3, 0.3, 0.5, 0.3, 0.01);
        }
    }
}
