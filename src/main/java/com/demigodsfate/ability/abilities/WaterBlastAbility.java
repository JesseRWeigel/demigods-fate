package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class WaterBlastAbility extends Ability {
    public WaterBlastAbility() {
        super("water_blast", "Water Blast", 200, 0, GodParent.POSEIDON, GodParent.NEPTUNE); // 10s cooldown
    }

    @Override
    public boolean execute(Player player, Level level) {
        Vec3 look = player.getLookAngle();
        Vec3 start = player.getEyePosition();

        // Knockback entities in a line in front of the player
        AABB area = player.getBoundingBox().inflate(8.0);
        for (Entity entity : level.getEntities(player, area)) {
            if (entity instanceof LivingEntity target) {
                Vec3 toEntity = target.position().subtract(player.position()).normalize();
                double dot = toEntity.dot(look);
                if (dot > 0.5 && target.distanceTo(player) < 10) {
                    target.push(look.x * 3, 0.5, look.z * 3);
                    target.hurt(player.damageSources().playerAttack(player), 6.0f);
                    target.hurtMarked = true;
                }
            }
        }

        // Particles and sound
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 30; i++) {
                double d = i * 0.3;
                serverLevel.sendParticles(ParticleTypes.SPLASH,
                        start.x + look.x * d, start.y + look.y * d, start.z + look.z * d,
                        5, 0.2, 0.2, 0.2, 0.1);
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 2.0f, 0.8f);
        return true;
    }
}
