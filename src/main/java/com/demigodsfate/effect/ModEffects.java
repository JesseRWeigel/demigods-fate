package com.demigodsfate.effect;

import com.demigodsfate.DemigodsFate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, DemigodsFate.MODID);

    // Stone Gaze — Medusa's petrifying stare (slowness + mining fatigue)
    public static final DeferredHolder<MobEffect, MobEffect> STONE_GAZE = MOB_EFFECTS.register(
            "stone_gaze",
            () -> new StoneGazeEffect(MobEffectCategory.HARMFUL, 0x808080));

    // Blessing of Ares — temporary combat boost
    public static final DeferredHolder<MobEffect, MobEffect> BLESSING_OF_ARES = MOB_EFFECTS.register(
            "blessing_of_ares",
            () -> new BlessingEffect(MobEffectCategory.BENEFICIAL, 0xFF0000));

    // Claimed — visual indicator after claiming ceremony
    public static final DeferredHolder<MobEffect, MobEffect> CLAIMED = MOB_EFFECTS.register(
            "claimed",
            () -> new BlessingEffect(MobEffectCategory.BENEFICIAL, 0xFFD700));
}
