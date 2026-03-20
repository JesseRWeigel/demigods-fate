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

/**
 * Mars Slot 1 — Total damage immunity for 5 seconds (Resistance V).
 */
public class ShieldWallAbility extends Ability {
    private static final int DURATION_TICKS = 100; // 5 seconds

    public ShieldWallAbility() {
        super("shield_wall", "Shield Wall", 150, 1, GodParent.MARS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Resistance V = nearly total damage immunity
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, DURATION_TICKS, 4, false, true));
        // Fire resistance as well
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, DURATION_TICKS, 0, false, true));

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.5F, 0.7F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.5F, 1.5F);

        // Shield wall particle effect — ring of particles around the player
        for (int angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            double px = player.getX() + Math.cos(rad) * 1.5;
            double pz = player.getZ() + Math.sin(rad) * 1.5;
            for (double y = 0; y < 2.5; y += 0.5) {
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        px, player.getY() + y, pz,
                        1, 0.0, 0.0, 0.0, 0.0);
            }
        }

        serverLevel.sendParticles(ParticleTypes.FLASH,
                player.getX(), player.getY() + 1, player.getZ(),
                1, 0.0, 0.0, 0.0, 0.0);

        return true;
    }
}
