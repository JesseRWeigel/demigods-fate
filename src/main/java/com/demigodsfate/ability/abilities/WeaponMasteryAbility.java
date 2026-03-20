package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Temporary max attack speed. */
public class WeaponMasteryAbility extends Ability {
    public WeaponMasteryAbility() {
        super("weapon_mastery", "Weapon Mastery", 300, 2, GodParent.ARES, GodParent.MARS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2)); // Haste for attack speed
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
        return true;
    }
}
