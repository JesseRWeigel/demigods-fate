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
 * Pluto Slot 2 — Life drain: damage target and heal self.
 */
public class DeathGraspAbility extends Ability {
    private static final double RANGE = 15.0;
    private static final float DAMAGE = 10.0F;
    private static final float HEAL_AMOUNT = 8.0F;

    public DeathGraspAbility() {
        super("death_grasp", "Death Grasp", 400, 2, GodParent.PLUTO);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Find entity the player is looking at
        Vec3 lookVec = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();
        AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(RANGE)).inflate(2.0);

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                e -> e != player && e.isAlive());

        // Find closest entity along look direction
        LivingEntity target = null;
        double closestDist = Double.MAX_VALUE;
        for (LivingEntity entity : entities) {
            Vec3 toEntity = entity.getEyePosition().subtract(eyePos);
            double dot = toEntity.normalize().dot(lookVec);
            if (dot > 0.8) { // Within ~37 degree cone
                double dist = toEntity.lengthSqr();
                if (dist < closestDist) {
                    closestDist = dist;
                    target = entity;
                }
            }
        }

        if (target == null) return false;

        // Damage target
        target.hurt(level.damageSources().magic(), DAMAGE);

        // Heal player
        player.heal(HEAL_AMOUNT);

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.8F, 0.5F);
        level.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.WITHER_HURT, SoundSource.HOSTILE, 0.6F, 0.8F);

        // Life drain particle beam from target to player
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
        Vec3 playerPos = player.position().add(0, player.getBbHeight() / 2, 0);
        Vec3 diff = playerPos.subtract(targetPos);
        double dist = diff.length();

        for (double d = 0; d < dist; d += 0.3) {
            double t = d / dist;
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    targetPos.x + diff.x * t,
                    targetPos.y + diff.y * t,
                    targetPos.z + diff.z * t,
                    1, 0.05, 0.05, 0.05, 0.0);
        }

        // Dark particles around target
        serverLevel.sendParticles(ParticleTypes.SOUL,
                target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                15, 0.5, 0.5, 0.5, 0.05);

        // Healing particles on player
        serverLevel.sendParticles(ParticleTypes.HEART,
                player.getX(), player.getY() + 1.5, player.getZ(),
                5, 0.3, 0.3, 0.3, 0.0);

        return true;
    }
}
