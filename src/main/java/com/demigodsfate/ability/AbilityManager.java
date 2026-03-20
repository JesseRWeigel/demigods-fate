package com.demigodsfate.ability;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Handles ability execution with cooldown management.
 * Called when a player presses an ability keybind (R=0, V=1, G=2).
 */
public class AbilityManager {

    /**
     * Attempt to use an ability for the given slot.
     * @param player The player
     * @param slot Ability slot (0=R, 1=V, 2=G)
     */
    public static void tryUseAbility(Player player, int slot) {
        if (player.level().isClientSide()) return;

        GodParent god = GodParentData.getGodParent(player);
        if (god == null) {
            player.sendSystemMessage(Component.literal("You must be claimed by a god first!")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        Ability ability = AbilityRegistry.getAbility(god, slot);
        if (ability == null) {
            player.sendSystemMessage(Component.literal("No ability in slot " + (slot + 1))
                    .withStyle(ChatFormatting.RED));
            return;
        }

        // Check cooldown
        if (!GodParentData.isAbilityReady(player, ability.getId())) {
            long remaining = GodParentData.getAbilityCooldown(player, ability.getId()) - player.level().getGameTime();
            int seconds = (int) Math.ceil(remaining / 20.0);
            player.sendSystemMessage(Component.literal(ability.getDisplayName() + " on cooldown: " + seconds + "s")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        // Execute ability
        Level level = player.level();
        boolean success = ability.execute(player, level);

        if (success) {
            // Set cooldown
            long cooldownEnd = level.getGameTime() + ability.getCooldownTicks();
            GodParentData.setAbilityCooldown(player, ability.getId(), cooldownEnd);

            player.sendSystemMessage(Component.literal("⚡ " + ability.getDisplayName() + "!")
                    .withStyle(ChatFormatting.GOLD));
        }
    }
}
