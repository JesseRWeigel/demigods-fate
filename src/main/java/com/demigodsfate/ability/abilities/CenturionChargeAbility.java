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
 * Mars Slot 0 — Dash forward dealing damage to everything in path.
 */
public class CenturionChargeAbility extends Ability {
    private static final double DASH_DISTANCE = 10.0;
    private static final float DAMAGE = 8.0F;
    private static final double DASH_WIDTH = 2.0;

    public CenturionChargeAbility() {
        super("centurion_charge", "Centurion Charge", 300, 0, GodParent.MARS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        Vec3 lookVec = player.getLookAngle();
        Vec3 startPos = player.position();

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.RAVAGER_ATTACK, SoundSource.PLAYERS, 1.0F, 1.2F);

        // Calculate dash endpoint
        Vec3 dashVec = new Vec3(lookVec.x, 0, lookVec.z).normalize().scale(DASH_DISTANCE);
        Vec3 endPos = startPos.add(dashVec);

        // Collect all entities along the dash path
        AABB dashArea = player.getBoundingBox().expandTowards(dashVec).inflate(DASH_WIDTH);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, dashArea,
                e -> e != player && e.isAlive());

        for (LivingEntity entity : entities) {
            entity.hurt(level.damageSources().playerAttack(player), DAMAGE);
            // Knock entities to the side
            Vec3 knockDir = entity.position().subtract(player.position()).normalize();
            entity.setDeltaMovement(knockDir.x * 1.5, 0.4, knockDir.z * 1.5);
            entity.hurtMarked = true;
        }

        // Move the player forward
        player.setDeltaMovement(dashVec.normalize().scale(2.5).add(0, 0.1, 0));
        player.hurtMarked = true;

        // Particle trail
        for (double d = 0; d <= DASH_DISTANCE; d += 0.5) {
            Vec3 pos = startPos.add(dashVec.normalize().scale(d));
            serverLevel.sendParticles(ParticleTypes.FLAME,
                    pos.x, pos.y + 0.5, pos.z,
                    3, 0.3, 0.2, 0.3, 0.01);
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    pos.x, pos.y + 1.0, pos.z,
                    2, 0.2, 0.2, 0.2, 0.05);
        }

        return true;
    }
}
