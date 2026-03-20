package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Temporary damage reduction for the player. */
public class AegisAuraAbility extends Ability {
    public AegisAuraAbility() {
        super("aegis_aura", "Aegis Aura", 300, 1, GodParent.ATHENA, GodParent.MINERVA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1));
        return true;
    }
}
