package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

/**
 * Minerva Slot 1 — Reveal all hidden ores/chests nearby (glowing effect on player + particle markers).
 */
public class KnowledgePulseAbility extends Ability {
    private static final int SCAN_RADIUS = 16;

    private static final Set<Block> VALUABLE_BLOCKS = Set.of(
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL,
            Blocks.SPAWNER
    );

    public KnowledgePulseAbility() {
        super("knowledge_pulse", "Knowledge Pulse", 200, 1, GodParent.MINERVA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Give player glowing + night vision to see better
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,  100, 0, false, true));

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.5F);

        // Expanding pulse particle
        for (double r = 1.0; r <= SCAN_RADIUS; r += 2.0) {
            for (int angle = 0; angle < 360; angle += 30) {
                double rad = Math.toRadians(angle);
                serverLevel.sendParticles(ParticleTypes.ENCHANT,
                        player.getX() + Math.cos(rad) * r,
                        player.getY() + 1,
                        player.getZ() + Math.sin(rad) * r,
                        2, 0.2, 0.2, 0.2, 0.0);
            }
        }

        // Scan for valuable blocks and mark them with particles
        BlockPos playerPos = player.blockPosition();
        int found = 0;

        for (int x = -SCAN_RADIUS; x <= SCAN_RADIUS; x++) {
            for (int y = -SCAN_RADIUS; y <= SCAN_RADIUS; y++) {
                for (int z = -SCAN_RADIUS; z <= SCAN_RADIUS; z++) {
                    BlockPos checkPos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);

                    if (VALUABLE_BLOCKS.contains(state.getBlock())) {
                        // Mark with bright particles
                        serverLevel.sendParticles(ParticleTypes.GLOW,
                                checkPos.getX() + 0.5, checkPos.getY() + 0.5, checkPos.getZ() + 0.5,
                                5, 0.2, 0.2, 0.2, 0.0);
                        serverLevel.sendParticles(ParticleTypes.END_ROD,
                                checkPos.getX() + 0.5, checkPos.getY() + 1.0, checkPos.getZ() + 0.5,
                                3, 0.1, 0.3, 0.1, 0.02);
                        found++;
                    }
                }
            }
        }

        // Feedback sound based on results
        if (found > 0) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.2F);
        }

        return true;
    }
}
