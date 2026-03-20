package com.demigodsfate.entity.npc;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.quest.QuestManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Chiron — the immortal centaur, activities director of Camp Half-Blood.
 * Gives quests, training advice, and lore about the gods.
 */
public class ChironEntity extends CampNpcEntity {
    public ChironEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, "Chiron",
                "Welcome to Camp Half-Blood, young demigod.",
                "The gods have many enemies. You must prepare.",
                "Train well. The Oracle will guide your path.",
                "Remember — a hero's greatest weapon is their mind.",
                "The prophecy speaks of you... I can feel it.",
                "Visit the Oracle in the Big House when you are ready.");
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.level().isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.SUCCESS;
        }

        // Special dialogue based on player state
        if (!GodParentData.isClaimed(player)) {
            player.sendSystemMessage(Component.literal("[Chiron] ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal("Ah, an unclaimed one. The gods will reveal your parentage soon. Use ")
                            .withStyle(ChatFormatting.WHITE))
                    .append(Component.literal("/demigod claim <god>")
                            .withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(" to discover your heritage.")
                            .withStyle(ChatFormatting.WHITE)));
            return InteractionResult.SUCCESS;
        }

        GodParent god = GodParentData.getGodParent(player);
        String activeQuest = QuestManager.getActiveQuestId(player);

        if (activeQuest == null) {
            player.sendSystemMessage(Component.literal("[Chiron] ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal("Child of " + god.getDisplayName() + ", you should speak to the Oracle. A quest awaits you.")
                            .withStyle(ChatFormatting.WHITE)));
            player.sendSystemMessage(Component.literal("  Use: /quest start the_lightning_thief")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else {
            QuestManager.showStatus(player);
            player.sendSystemMessage(Component.literal("[Chiron] ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal("Stay focused on your quest. The fate of Olympus may depend on it.")
                            .withStyle(ChatFormatting.WHITE)));
        }

        return InteractionResult.SUCCESS;
    }
}
