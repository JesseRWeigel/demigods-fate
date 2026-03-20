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
 * Minerva Slot 2 — Grant a luck effect so the next crafted item gets a random enchantment.
 * Applies Luck III for 30 seconds. Crafting while under this effect may yield better results.
 */
public class PerfectCraftAbility extends Ability {
    private static final int DURATION_TICKS = 600; // 30 seconds

    public PerfectCraftAbility() {
        super("perfect_craft", "Perfect Craft", 300, 2, GodParent.MINERVA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Apply Luck III — affects loot tables and can be used to trigger enchantment logic
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, DURATION_TICKS, 2, false, true));

        // Also apply Hero of the Village for better trades
        player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, DURATION_TICKS, 1, false, true));

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.7F, 1.5F);

        // Enchantment particles swirling around player
        for (int i = 0; i < 5; i++) {
            double yOffset = i * 0.4;
            for (int angle = 0; angle < 360; angle += 20) {
                double rad = Math.toRadians(angle + i * 30);
                double radius = 1.0 + i * 0.2;
                serverLevel.sendParticles(ParticleTypes.ENCHANT,
                        player.getX() + Math.cos(rad) * radius,
                        player.getY() + yOffset + 0.5,
                        player.getZ() + Math.sin(rad) * radius,
                        1, 0.0, 0.1, 0.0, 0.0);
            }
        }

        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                player.getX(), player.getY() + 2, player.getZ(),
                10, 0.5, 0.3, 0.5, 0.0);

        return true;
    }
}
