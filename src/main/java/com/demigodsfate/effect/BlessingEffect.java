package com.demigodsfate.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Generic beneficial effect used for various god blessings.
 */
public class BlessingEffect extends MobEffect {
    public BlessingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
