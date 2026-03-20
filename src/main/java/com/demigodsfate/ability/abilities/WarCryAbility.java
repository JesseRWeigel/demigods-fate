package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** AoE fear — nearby mobs flee for a few seconds. */
public class WarCryAbility extends Ability {
    public WarCryAbility() {
        super("war_cry", "War Cry", 400, 1, GodParent.ARES, GodParent.MARS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        AABB area = player.getBoundingBox().inflate(8.0);
        for (Entity entity : level.getEntities(player, area)) {
            if (entity instanceof Mob mob && mob.distanceTo(player) < 8) {
                // Push mobs away (fear)
                Vec3 away = mob.position().subtract(player.position()).normalize().scale(2.5);
                mob.push(away.x, 0.3, away.z);
                mob.hurtMarked = true;
                // Apply weakness (they're terrified)
                mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
            }
        }

        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                    player.getX(), player.getY() + 1, player.getZ(),
                    20, 3.0, 1.0, 3.0, 0.0);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 2.0f, 0.7f);
        return true;
    }
}
