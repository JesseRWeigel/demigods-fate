package com.demigodsfate.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

/**
 * Armor materials for the mod.
 * Uses vanilla ArmorMaterials as bases since custom registration
 * requires careful timing with NeoForge's registry freeze.
 *
 * Celestial Bronze = IRON tier (between iron and diamond)
 * Imperial Gold = DIAMOND tier
 */
public class ModArmorMaterials {
    // Celestial Bronze = DIAMOND tier (stronger than vanilla iron/diamond)
    public static final Holder<ArmorMaterial> CELESTIAL_BRONZE = ArmorMaterials.DIAMOND;
    // Imperial Gold = NETHERITE tier (strongest vanilla tier)
    public static final Holder<ArmorMaterial> IMPERIAL_GOLD = ArmorMaterials.NETHERITE;

    public static void init() {
        // Force class loading
    }
}
