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

public class WindGustAbility extends Ability {
    public WindGustAbility() {
        super("wind_gust", "Wind Gust", 200, 1, GodParent.ZEUS, GodParent.JUPITER);
    }

    @Override
    public boolean execute(Player player, Level level) {
        // Push all nearby entities away + launch player upward (flight burst)
        Vec3 look = player.getLookAngle();
        AABB area = player.getBoundingBox().inflate(5.0);

        for (Entity entity : level.getEntities(player, area)) {
            if (entity instanceof LivingEntity target && target.distanceTo(player) < 5) {
                Vec3 push = target.position().subtract(player.position()).normalize().scale(2.0);
                target.push(push.x, 0.5, push.z);
                target.hurtMarked = true;
            }
        }

        // Launch player upward
        player.push(look.x * 0.5, 1.5, look.z * 0.5);
        player.hurtMarked = true;
        player.resetFallDistance();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY(), player.getZ(),
                    30, 2.0, 0.5, 2.0, 0.1);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 2.0f, 1.0f);
        return true;
    }
}
