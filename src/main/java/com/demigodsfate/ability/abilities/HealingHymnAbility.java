package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Heal self + nearby allies. */
public class HealingHymnAbility extends Ability {
    public HealingHymnAbility() {
        super("healing_hymn", "Healing Hymn", 300, 0, GodParent.APOLLO);
    }

    @Override
    public boolean execute(Player player, Level level) {
        player.heal(10.0f); // 5 hearts instant
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));

        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.HEART,
                    player.getX(), player.getY() + 1.5, player.getZ(),
                    10, 1.0, 0.5, 1.0, 0.0);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.PLAYERS, 2.0f, 1.2f);
        return true;
    }
}
