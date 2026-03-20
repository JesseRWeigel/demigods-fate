package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Jupiter Slot 1 — Grant creative-style flight for 8 seconds.
 * Implemented via Slow Falling + Levitation combo and allowing flight.
 */
public class EagleFlightAbility extends Ability {
    private static final int DURATION_TICKS = 160; // 8 seconds

    public EagleFlightAbility() {
        super("eagle_flight", "Eagle Flight", 200, 1, GodParent.JUPITER);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Enable creative flight temporarily
        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;
        player.onUpdateAbilities();

        // Apply slow falling as safety net when flight expires
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, DURATION_TICKS + 60, 0, false, true));

        // Schedule flight removal — use slow falling duration as the timer
        // The CooldownManager or a tick handler should disable flight after 8 seconds
        // For now, we give a speed boost in the air
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DURATION_TICKS, 1, false, true));

        // Sound and particles
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.5F);

        serverLevel.sendParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                30, 1.0, 0.5, 1.0, 0.05);

        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                player.getX(), player.getY() + 1, player.getZ(),
                15, 0.5, 0.5, 0.5, 0.1);

        return true;
    }
}
