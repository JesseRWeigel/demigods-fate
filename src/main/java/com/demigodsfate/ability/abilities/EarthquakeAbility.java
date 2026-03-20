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

public class EarthquakeAbility extends Ability {
    public EarthquakeAbility() {
        super("earthquake", "Earthquake", 400, 1, GodParent.POSEIDON, GodParent.NEPTUNE); // 20s cooldown
    }

    @Override
    public boolean execute(Player player, Level level) {
        // AoE ground slam — damage and launch all nearby entities
        AABB area = player.getBoundingBox().inflate(6.0);
        for (Entity entity : level.getEntities(player, area)) {
            if (entity instanceof LivingEntity target && target.distanceTo(player) < 6) {
                target.hurt(player.damageSources().playerAttack(player), 8.0f);
                target.push(0, 1.0, 0);
                target.hurtMarked = true;
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 50; i++) {
                double angle = Math.random() * Math.PI * 2;
                double dist = Math.random() * 6;
                serverLevel.sendParticles(ParticleTypes.CLOUD,
                        player.getX() + Math.cos(angle) * dist,
                        player.getY() + 0.1,
                        player.getZ() + Math.sin(angle) * dist,
                        3, 0.2, 0.5, 0.2, 0.05);
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.5f, 0.5f);
        return true;
    }
}
