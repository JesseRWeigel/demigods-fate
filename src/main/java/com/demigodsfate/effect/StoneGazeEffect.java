package com.demigodsfate.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;
import com.demigodsfate.DemigodsFate;

/**
 * Stone Gaze — applied when looking at Medusa.
 * Causes extreme slowness (turning to stone) and mining fatigue.
 */
public class StoneGazeEffect extends MobEffect {
    public StoneGazeEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "stone_gaze_slow"),
                -0.9, // 90% slower
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "stone_gaze_attack_slow"),
                -0.8, // 80% slower attack
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // At high amplifier (3+), deal damage (turning to stone)
        if (amplifier >= 3) {
            entity.hurt(entity.damageSources().magic(), 2.0f);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Apply damage every second (20 ticks)
        return duration % 20 == 0;
    }
}
