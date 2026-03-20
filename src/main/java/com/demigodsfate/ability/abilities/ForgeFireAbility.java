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

/** Ranged fire attack — cone of flames in front of the player. */
public class ForgeFireAbility extends Ability {
    public ForgeFireAbility() {
        super("forge_fire", "Forge Fire", 200, 0, GodParent.HEPHAESTUS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        Vec3 look = player.getLookAngle();
        AABB area = player.getBoundingBox().inflate(8.0);

        for (Entity entity : level.getEntities(player, area)) {
            if (entity instanceof LivingEntity target) {
                Vec3 toEntity = target.position().subtract(player.position()).normalize();
                double dot = toEntity.dot(look);
                if (dot > 0.3 && target.distanceTo(player) < 8) {
                    target.igniteForSeconds(5);
                    target.hurt(player.damageSources().onFire(), 4.0f);
                }
            }
        }

        if (level instanceof ServerLevel sl) {
            for (int i = 0; i < 40; i++) {
                double d = i * 0.2;
                sl.sendParticles(ParticleTypes.FLAME,
                        player.getX() + look.x * d, player.getEyeY() + look.y * d, player.getZ() + look.z * d,
                        3, 0.3, 0.3, 0.3, 0.02);
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.5f, 1.0f);
        return true;
    }
}
