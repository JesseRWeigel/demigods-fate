package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HurricaneFormAbility extends Ability {
    public HurricaneFormAbility() {
        super("hurricane_form", "Hurricane Form", 600, 2, GodParent.POSEIDON, GodParent.NEPTUNE); // 30s cooldown
    }

    @Override
    public boolean execute(Player player, Level level) {
        // Speed + damage aura for 15 seconds
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 2));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0));
        return true;
    }
}
