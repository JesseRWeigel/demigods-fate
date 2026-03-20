package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Jupiter Slot 0 — Throw a lightning javelin projectile that explodes on impact with lightning.
 */
public class LightningJavelinAbility extends Ability {
    public LightningJavelinAbility() {
        super("lightning_javelin", "Lightning Javelin", 300, 0, GodParent.JUPITER);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Raycast to where the player is looking (up to 40 blocks)
        HitResult hit = player.pick(40.0, 0.0f, false);
        BlockPos target;
        if (hit.getType() == HitResult.Type.BLOCK) {
            target = ((BlockHitResult) hit).getBlockPos();
        } else {
            target = player.blockPosition().offset(
                    (int) (player.getLookAngle().x * 20),
                    0,
                    (int) (player.getLookAngle().z * 20));
        }

        // Play throw sound at player
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 0.8F);

        // Spawn lightning at impact point
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
        if (bolt != null) {
            bolt.moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
            bolt.setCause(player instanceof net.minecraft.server.level.ServerPlayer sp ? sp : null);
            serverLevel.addFreshEntity(bolt);
        }

        // Particle trail from player to target
        double dx = target.getX() + 0.5 - player.getX();
        double dy = target.getY() + 0.5 - player.getEyeY();
        double dz = target.getZ() + 0.5 - player.getZ();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        for (int i = 0; i < (int) dist * 2; i++) {
            double t = i / (dist * 2);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    player.getX() + dx * t,
                    player.getEyeY() + dy * t,
                    player.getZ() + dz * t,
                    2, 0.1, 0.1, 0.1, 0.0);
        }

        // Explosion particles at impact
        serverLevel.sendParticles(ParticleTypes.FLASH,
                target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5,
                3, 0.5, 0.5, 0.5, 0.0);

        return true;
    }
}
