package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Short-range teleport in the direction the player is looking. */
public class ShadowStepAbility extends Ability {
    public ShadowStepAbility() {
        super("shadow_step", "Shadow Step", 100, 0, GodParent.HERMES, GodParent.PLUTO); // 5s cooldown
    }

    @Override
    public boolean execute(Player player, Level level) {
        Vec3 look = player.getLookAngle();
        Vec3 target = player.position().add(look.scale(8)); // 8 blocks forward

        // Teleport to target
        if (level instanceof ServerLevel) {
            // Particles at departure
            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL,
                    player.getX(), player.getY() + 1, player.getZ(),
                    20, 0.3, 0.5, 0.3, 0.5);

            player.teleportTo(target.x, target.y, target.z);
            player.resetFallDistance();

            // Particles at arrival
            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL,
                    target.x, target.y + 1, target.z,
                    20, 0.3, 0.5, 0.3, 0.5);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.2f);
        return true;
    }
}
