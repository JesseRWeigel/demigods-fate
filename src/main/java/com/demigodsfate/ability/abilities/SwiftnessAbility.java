package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Burst of extreme speed. */
public class SwiftnessAbility extends Ability {
    public SwiftnessAbility() {
        super("swiftness", "Swiftness", 400, 2, GodParent.HERMES);
    }

    @Override
    public boolean execute(Player player, Level level) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 4)); // Speed V for 10 seconds!
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 2));

        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY(), player.getZ(),
                    15, 0.5, 0.1, 0.5, 0.1);
        }
        return true;
    }
}
