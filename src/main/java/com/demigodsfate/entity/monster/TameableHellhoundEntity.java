package com.demigodsfate.entity.monster;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Mixin-style taming behavior added to HellhoundEntity.
 * Feed a Hellhound bone + stygian iron/ambrosia while a child of Hades/Pluto to tame it.
 * Like Mrs. O'Leary from the books!
 */
public class TameableHellhoundEntity {

    /**
     * Call this from HellhoundEntity.mobInteract to handle taming.
     * Returns true if the interaction was handled.
     */
    public static boolean tryTame(HellhoundEntity hellhound, Player player, InteractionHand hand) {
        if (player.level().isClientSide()) return false;

        ItemStack held = player.getItemInHand(hand);

        // Must hold bone or ambrosia to tame
        boolean isTameItem = held.is(Items.BONE) || held.is(ModItems.AMBROSIA.get());
        if (!isTameItem) return false;

        // Higher chance for Pluto/Hades children (they control the dead)
        GodParent god = GodParentData.getGodParent(player);
        float tameChance = 0.15f; // 15% base chance
        if (god == GodParent.PLUTO) tameChance = 0.5f; // 50% for Pluto!
        if (held.is(ModItems.AMBROSIA.get())) tameChance += 0.2f; // Ambrosia helps

        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }

        if (player.getRandom().nextFloat() < tameChance) {
            // Tamed!
            hellhound.setTarget(null);
            hellhound.setPersistenceRequired();
            hellhound.setCustomName(Component.literal("Mrs. O'Leary").withStyle(ChatFormatting.DARK_RED));
            hellhound.setCustomNameVisible(true);

            // Make it follow the player by removing player-targeting AI
            // (It will still attack other monsters but not the player)
            hellhound.targetSelector.removeAllGoals(g -> true);

            if (hellhound.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.HEART,
                        hellhound.getX(), hellhound.getY() + 1, hellhound.getZ(),
                        10, 0.5, 0.5, 0.5, 0.0);
            }

            player.sendSystemMessage(Component.literal("The Hellhound licks your hand! You've tamed it!")
                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Mrs. O'Leary will fight by your side!")
                    .withStyle(ChatFormatting.DARK_RED));
            return true;
        } else {
            // Failed but didn't anger it
            if (hellhound.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.SMOKE,
                        hellhound.getX(), hellhound.getY() + 0.5, hellhound.getZ(),
                        5, 0.3, 0.3, 0.3, 0.01);
            }
            player.sendSystemMessage(Component.literal("The Hellhound sniffs your offering but isn't convinced...")
                    .withStyle(ChatFormatting.GRAY));
            return true;
        }
    }
}
