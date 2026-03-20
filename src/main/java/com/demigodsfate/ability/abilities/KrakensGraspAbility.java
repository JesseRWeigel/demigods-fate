package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Neptune Slot 2 — Root all enemies in place with extreme slowness + levitation.
 */
public class KrakensGraspAbility extends Ability {
    private static final double RANGE = 10.0;
    private static final int DURATION_TICKS = 100; // 5 seconds

    public KrakensGraspAbility() {
        super("krakens_grasp", "Kraken's Grasp", 600, 2, GodParent.NEPTUNE);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        AABB area = player.getBoundingBox().inflate(RANGE);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive() && e.distanceTo(player) <= RANGE);

        if (entities.isEmpty()) return false;

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.0F, 0.5F);

        for (LivingEntity entity : entities) {
            // Extreme slowness to root in place
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, DURATION_TICKS, 4, false, true));
            // Slight levitation for the "grasped" feeling
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, DURATION_TICKS, 0, false, true));
            // Mining fatigue to prevent block breaking
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, DURATION_TICKS, 3, false, true));

            // Tentacle-like particles around each entity
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(i * 45);
                serverLevel.sendParticles(ParticleTypes.SQUID_INK,
                        entity.getX() + Math.cos(angle) * 1.5,
                        entity.getY() + 0.5,
                        entity.getZ() + Math.sin(angle) * 1.5,
                        5, 0.1, 0.3, 0.1, 0.02);
            }

            serverLevel.sendParticles(ParticleTypes.BUBBLE,
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(),
                    20, 0.5, 0.5, 0.5, 0.05);
        }

        return true;
    }
}
