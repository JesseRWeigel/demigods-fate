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
    // Use vanilla materials as holders — the actual defense values
    // come from the ArmorItem.Type durability multiplier in ModItems
    public static final Holder<ArmorMaterial> CELESTIAL_BRONZE = ArmorMaterials.IRON;
    public static final Holder<ArmorMaterial> IMPERIAL_GOLD = ArmorMaterials.DIAMOND;

    public static void init() {
        // Force class loading
    }
}
