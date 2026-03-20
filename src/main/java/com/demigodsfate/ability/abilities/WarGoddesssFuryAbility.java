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
 * Bellona Slot 0 — Multi-hit combo: rapidly damage everything in front.
 */
public class WarGoddesssFuryAbility extends Ability {
    private static final double RANGE = 5.0;
    private static final float DAMAGE_PER_HIT = 4.0F;
    private static final int HIT_COUNT = 5;
    private static final double CONE_ANGLE = 0.5; // dot product threshold (~60 degree cone)

    public WarGoddesssFuryAbility() {
        super("war_goddesss_fury", "War Goddess's Fury", 200, 0, GodParent.BELLONA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        Vec3 lookVec = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();

        // Find all entities in front of the player
        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive());

        // Filter to entities in front (within cone)
        List<LivingEntity> targets = entities.stream().filter(e -> {
            Vec3 toEntity = e.getEyePosition().subtract(eyePos).normalize();
            return toEntity.dot(lookVec) > CONE_ANGLE && e.distanceTo(player) <= RANGE;
        }).toList();

        if (targets.isEmpty()) return false;

        // Multi-hit combo — each hit with sound and particles
        for (int hit = 0; hit < HIT_COUNT; hit++) {
            for (LivingEntity target : targets) {
                target.hurt(level.damageSources().playerAttack(player), DAMAGE_PER_HIT);
                target.invulnerableTime = 0; // Reset invulnerability for multi-hit

                // Sweep particles per hit
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                        target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                        1, 0.2, 0.2, 0.2, 0.0);
            }

            // Sound per swing
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F + hit * 0.1F);
        }

        // Final impact particles
        serverLevel.sendParticles(ParticleTypes.FLAME,
                player.getX() + lookVec.x * 2, player.getY() + 1, player.getZ() + lookVec.z * 2,
                20, 1.0, 0.5, 1.0, 0.05);

        serverLevel.sendParticles(ParticleTypes.CRIT,
                player.getX() + lookVec.x * 2, player.getY() + 1, player.getZ() + lookVec.z * 2,
                15, 1.0, 0.5, 1.0, 0.1);

        return true;
    }
}
