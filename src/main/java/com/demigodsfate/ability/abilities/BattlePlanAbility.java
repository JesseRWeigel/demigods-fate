package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Reveals all nearby mobs through walls (glowing effect on nearby entities). */
public class BattlePlanAbility extends Ability {
    public BattlePlanAbility() {
        super("battle_plan", "Battle Plan", 200, 0, GodParent.ATHENA, GodParent.MINERVA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        // Apply Glowing to all living entities within 30 blocks
        level.getEntities(player, player.getBoundingBox().inflate(30.0)).forEach(entity -> {
            if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING,  100, 0));
            }
        });
        return true;
    }
}
