package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/** Poison AoE around a target area. */
public class PlagueShotAbility extends Ability {
    public PlagueShotAbility() {
        super("plague_shot", "Plague Shot", 400, 2, GodParent.APOLLO);
    }

    @Override
    public boolean execute(Player player, Level level) {
        // Poison all entities within 6 blocks
        AABB area = player.getBoundingBox().inflate(6.0);
        for (Entity entity : level.getEntities(player, area)) {
            if (entity instanceof LivingEntity target && target.distanceTo(player) < 6) {
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
            }
        }

        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.ITEM_SLIME,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    40, 4.0, 1.0, 4.0, 0.05);
        }
        return true;
    }
}
