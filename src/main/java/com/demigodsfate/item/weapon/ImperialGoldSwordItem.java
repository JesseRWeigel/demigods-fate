package com.demigodsfate.item.weapon;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * Imperial Gold sword — damages everything (monsters AND mortals).
 * Higher damage than Celestial Bronze but less durable.
 */
public class ImperialGoldSwordItem extends SwordItem {
    public ImperialGoldSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
    // Imperial Gold has no mortal-pass-through restriction — damages everything.
    // The higher damage and enchantability come from the tier definition.
}
