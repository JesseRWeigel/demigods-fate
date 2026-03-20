package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Pluto Slot 1 — Invisibility + speed for 10 seconds.
 */
public class ShadowMeldAbility extends Ability {
    private static final int DURATION_TICKS = 200; // 10 seconds

    public ShadowMeldAbility() {
        super("shadow_meld", "Shadow Meld", 150, 1, GodParent.PLUTO);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Apply invisibility and speed
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, DURATION_TICKS, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DURATION_TICKS, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, DURATION_TICKS + 20, 0, false, false));

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 0.5F);

        // Shadow particles — player dissolves into darkness
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                player.getX(), player.getY() + 1, player.getZ(),
                40, 0.5, 1.0, 0.5, 0.02);

        serverLevel.sendParticles(ParticleTypes.SOUL,
                player.getX(), player.getY(), player.getZ(),
                15, 0.3, 0.5, 0.3, 0.05);

        serverLevel.sendParticles(ParticleTypes.ASH,
                player.getX(), player.getY() + 1, player.getZ(),
                30, 1.0, 1.0, 1.0, 0.0);

        return true;
    }
}
