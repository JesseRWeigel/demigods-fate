package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Neptune Slot 0 — Trident-like ranged attack with water damage.
 */
public class TridentStrikeAbility extends Ability {
    private static final double RANGE = 25.0;
    private static final float DAMAGE = 10.0F;

    public TridentStrikeAbility() {
        super("trident_strike", "Trident Strike",  100, 0, GodParent.NEPTUNE);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        Vec3 lookVec = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);

        // Trace a line and damage the first entity hit
        boolean hitSomething = false;
        for (double d = 1.0; d <= RANGE; d += 0.5) {
            Vec3 pos = eyePos.add(lookVec.scale(d));

            // Water particles along path
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    pos.x, pos.y, pos.z, 3, 0.1, 0.1, 0.1, 0.0);
            serverLevel.sendParticles(ParticleTypes.BUBBLE,
                    pos.x, pos.y, pos.z, 1, 0.1, 0.1, 0.1, 0.0);

            // Check for entity at this position
            AABB hitBox = new AABB(pos.x - 0.5, pos.y - 0.5, pos.z - 0.5,
                    pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, hitBox,
                    e -> e != player && e.isAlive());

            if (!entities.isEmpty()) {
                LivingEntity target = entities.get(0);
                target.hurt(level.damageSources().trident(player, player), DAMAGE);
                target.setDeltaMovement(lookVec.scale(0.8));
                target.hurtMarked = true;

                // Impact particles
                serverLevel.sendParticles(ParticleTypes.SPLASH,
                        target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                        30, 0.5, 0.5, 0.5, 0.2);

                level.playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                hitSomething = true;
                break;
            }
        }

        // Still counts as used even if no target hit (it's a ranged attack)
        return true;
    }
}
