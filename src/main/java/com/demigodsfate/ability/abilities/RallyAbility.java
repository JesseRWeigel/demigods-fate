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
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Bellona Slot 1 — Buff all nearby players with Strength + Speed.
 */
public class RallyAbility extends Ability {
    private static final double RANGE = 16.0;
    private static final int DURATION_TICKS = 300; // 15 seconds

    public RallyAbility() {
        super("rally", "Rally", 200, 1, GodParent.BELLONA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Find all nearby players (including self)
        AABB area = player.getBoundingBox().inflate(RANGE);
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, area,
                p -> p.isAlive() && p.distanceTo(player) <= RANGE);

        // Sound — war horn
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GOAT_HORN_PLAY, SoundSource.PLAYERS, 2.0F, 0.8F);

        for (Player target : nearbyPlayers) {
            // Apply buffs
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, DURATION_TICKS, 1, false, true));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DURATION_TICKS, 1, false, true));
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, DURATION_TICKS, 0, false, true));

            // Particles on each buffed player
            serverLevel.sendParticles(ParticleTypes.FLAME,
                    target.getX(), target.getY() + 2.2, target.getZ(),
                    10, 0.3, 0.1, 0.3, 0.02);
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    target.getX(), target.getY() + 1, target.getZ(),
                    8, 0.5, 0.5, 0.5, 0.0);
        }

        // War banner particle effect — pillar of fire above caster
        for (double y = 0; y < 5; y += 0.3) {
            serverLevel.sendParticles(ParticleTypes.FLAME,
                    player.getX(), player.getY() + y, player.getZ(),
                    2, 0.15, 0.05, 0.15, 0.01);
        }

        return true;
    }
}
