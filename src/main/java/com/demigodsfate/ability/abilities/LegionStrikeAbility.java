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

import java.util.List;

/**
 * Mars Slot 2 — AoE sword slash hitting all enemies in 8 block radius.
 */
public class LegionStrikeAbility extends Ability {
    private static final double RANGE = 8.0;
    private static final float DAMAGE = 12.0F;

    public LegionStrikeAbility() {
        super("legion_strike", "Legion Strike", 400, 2, GodParent.MARS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive() && e.distanceTo(player) <= RANGE);

        if (entities.isEmpty()) return false;

        // Sound — sweeping slash
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.5F, 0.7F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 0.5F, 1.5F);

        // Damage and knockback all entities
        for (LivingEntity entity : entities) {
            entity.hurt(level.damageSources().playerAttack(player), DAMAGE);

            // Knockback away from player
            double dx = entity.getX() - player.getX();
            double dz = entity.getZ() - player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist > 0) {
                entity.setDeltaMovement(dx / dist * 0.8, 0.3, dz / dist * 0.8);
                entity.hurtMarked = true;
            }

            // Sweep particles on each entity
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                    3, 0.3, 0.3, 0.3, 0.0);
        }

        // Circular sweep particle effect
        for (int angle = 0; angle < 360; angle += 8) {
            double rad = Math.toRadians(angle);
            for (double r = 2.0; r <= RANGE; r += 2.0) {
                serverLevel.sendParticles(ParticleTypes.CRIT,
                        player.getX() + Math.cos(rad) * r,
                        player.getY() + 1.0,
                        player.getZ() + Math.sin(rad) * r,
                        1, 0.1, 0.1, 0.1, 0.02);
            }
        }

        return true;
    }
}
