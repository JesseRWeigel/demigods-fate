package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

/**
 * Jupiter Slot 2 — Command nearby mobs to fight for you (make them target your target).
 */
public class ImperiumAbility extends Ability {
    private static final double RANGE = 16.0;

    public ImperiumAbility() {
        super("imperium", "Imperium", 600, 2, GodParent.JUPITER);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Determine the target the player is looking at
        HitResult hit = player.pick(30.0, 0.0f, false);
        LivingEntity target = null;

        // Check for entity hit via AABB scan along look vector
        AABB lookBox = player.getBoundingBox().expandTowards(
                player.getLookAngle().scale(30.0)).inflate(2.0);
        List<LivingEntity> potentialTargets = level.getEntitiesOfClass(LivingEntity.class, lookBox,
                e -> e != player && e.isAlive());

        double closestDist = Double.MAX_VALUE;
        for (LivingEntity entity : potentialTargets) {
            double dist = player.distanceToSqr(entity);
            if (dist < closestDist) {
                closestDist = dist;
                target = entity;
            }
        }

        if (target == null) return false;

        // Find all nearby mobs and command them to attack the target
        AABB area = player.getBoundingBox().inflate(RANGE);
        List<Mob> nearbyMobs = level.getEntitiesOfClass(Mob.class, area,
                mob -> mob.isAlive() && mob.distanceTo(player) <= RANGE);

        final LivingEntity finalTarget = target;
        int commanded = 0;
        for (Mob mob : nearbyMobs) {
            mob.setTarget(finalTarget);
            commanded++;

            // Particles on each commanded mob
            serverLevel.sendParticles(ParticleTypes.ENCHANT,
                    mob.getX(), mob.getY() + mob.getBbHeight() + 0.5, mob.getZ(),
                    10, 0.3, 0.3, 0.3, 0.1);
        }

        if (commanded == 0) return false;

        // Sound and particles on player
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 0.8F);

        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                player.getX(), player.getY() + 1, player.getZ(),
                20, 1.0, 1.0, 1.0, 0.05);

        return true;
    }
}
