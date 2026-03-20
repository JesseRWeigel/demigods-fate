package com.demigodsfate.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * Custom tool tiers for Percy Jackson weapon metals.
 *
 * Vanilla reference:
 *   Iron:    250 uses, 6.0 speed, 2.0 damage, level 2
 *   Diamond: 1561 uses, 8.0 speed, 3.0 damage, level 3
 *   Netherite: 2031 uses, 9.0 speed, 4.0 damage, level 4
 */
public class ModToolTiers {
    /**
     * Celestial Bronze: Above Iron, below Diamond.
     * Kills monsters but passes through mortals.
     */
    public static final Tier CELESTIAL_BRONZE = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,  // Can mine same blocks as iron+
            800,    // Durability (between iron 250 and diamond 1561)
            7.0f,   // Mining speed (between iron 6.0 and diamond 8.0)
            2.5f,   // Attack damage bonus (between iron 2.0 and diamond 3.0)
            16,     // Enchantability (same as iron)
            () -> Ingredient.of(ModItems.CELESTIAL_BRONZE_INGOT.get())
    );

    /**
     * Imperial Gold: Equal to Diamond tier.
     * More damage but less durable. Kills everything.
     */
    public static final Tier IMPERIAL_GOLD = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,  // Diamond-level mining
            1200,   // Durability (less than diamond's 1561 — less durable)
            8.0f,   // Mining speed (same as diamond)
            3.5f,   // Attack damage bonus (above diamond's 3.0)
            22,     // Enchantability (higher than diamond's 10)
            () -> Ingredient.of(ModItems.IMPERIAL_GOLD_INGOT.get())
    );

    /**
     * Stygian Iron: Above Diamond. Prevents monster respawn.
     */
    public static final Tier STYGIAN_IRON = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,  // Netherite-level mining
            1800,   // Durability
            9.0f,   // Mining speed
            4.0f,   // Attack damage bonus
            15,     // Enchantability
            () -> Ingredient.EMPTY  // Cannot be crafted — found only
    );
}
