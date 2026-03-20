package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Neptune Slot 1 — Push all nearby entities away with massive knockback.
 */
public class TidalWaveAbility extends Ability {
    private static final double RANGE = 12.0;
    private static final double KNOCKBACK_STRENGTH = 3.0;
    private static final float DAMAGE = 6.0F;

    public TidalWaveAbility() {
        super("tidal_wave", "Tidal Wave", 400, 1, GodParent.NEPTUNE);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 2.0F, 0.6F);

        // Expanding ring of water particles
        for (double r = 1.0; r <= RANGE; r += 1.0) {
            for (int angle = 0; angle < 360; angle += 15) {
                double rad = Math.toRadians(angle);
                double px = player.getX() + Math.cos(rad) * r;
                double pz = player.getZ() + Math.sin(rad) * r;
                serverLevel.sendParticles(ParticleTypes.SPLASH,
                        px, player.getY() + 0.5, pz,
                        2, 0.2, 0.3, 0.2, 0.0);
            }
        }

        // Bubble column particles
        serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP,
                player.getX(), player.getY(), player.getZ(),
                50, 2.0, 0.5, 2.0, 0.1);

        // Knockback all nearby entities
        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive() && e.distanceTo(player) <= RANGE);

        for (LivingEntity entity : entities) {
            Vec3 direction = entity.position().subtract(player.position()).normalize();
            double distance = entity.distanceTo(player);
            double scaledKnockback = KNOCKBACK_STRENGTH * (1.0 - distance / RANGE);

            entity.setDeltaMovement(direction.x * scaledKnockback,
                    0.6 + scaledKnockback * 0.3,
                    direction.z * scaledKnockback);
            entity.hurtMarked = true;
            entity.hurt(level.damageSources().playerAttack(player), DAMAGE);
        }

        return true;
    }
}
