package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Fire an explosive arrow imbued with sunlight. */
public class SunArrowAbility extends Ability {
    public SunArrowAbility() {
        super("sun_arrow", "Sun Arrow",  100, 1, GodParent.APOLLO);
    }

    @Override
    public boolean execute(Player player, Level level) {
        Vec3 look = player.getLookAngle();
        Arrow arrow = new Arrow(level, player, player.getMainHandItem(), null);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 3.0f, 0.0f);
        arrow.setBaseDamage(arrow.getBaseDamage() * 3.0); // Triple damage
        arrow.setCritArrow(true);
        arrow.igniteForSeconds(10);
        level.addFreshEntity(arrow);

        level.playSound(null, player.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.5f, 0.8f);
        return true;
    }
}
