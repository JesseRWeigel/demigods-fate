package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Damage + speed boost at the cost of health. */
public class BloodrageAbility extends Ability {
    public BloodrageAbility() {
        super("bloodrage", "Bloodrage", 150, 0, GodParent.ARES, GodParent.MARS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        // Cost: 4 hearts of health
        player.hurt(player.damageSources().magic(), 8.0f);
        // Gain: massive damage and speed for 10 seconds
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,  100, 2));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,  100, 1));
        return true;
    }
}
