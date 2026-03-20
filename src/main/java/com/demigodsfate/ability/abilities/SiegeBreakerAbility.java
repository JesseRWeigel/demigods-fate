package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Bellona Slot 2 — Destroy blocks in a 3x3x3 area where you're looking.
 */
public class SiegeBreakerAbility extends Ability {
    private static final int RADIUS = 1; // 3x3x3 = radius 1 around center

    public SiegeBreakerAbility() {
        super("siege_breaker", "Siege Breaker", 300, 2, GodParent.BELLONA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Raycast to where the player is looking
        HitResult hit = player.pick(20.0, 0.0f, false);
        if (hit.getType() != HitResult.Type.BLOCK) return false;

        BlockPos center = ((BlockHitResult) hit).getBlockPos();

        // Sound
        level.playSound(null, center.getX(), center.getY(), center.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 0.8F);

        // Destroy 3x3x3 area
        int destroyed = 0;
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(targetPos);

                    // Don't destroy bedrock or unbreakable blocks
                    if (state.isAir() || state.getDestroySpeed(level, targetPos) < 0) continue;

                    // Drop the block as item
                    level.destroyBlock(targetPos, true, player);
                    destroyed++;
                }
            }
        }

        if (destroyed == 0) return false;

        // Explosion particles
        serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5,
                5, 1.0, 1.0, 1.0, 0.0);

        serverLevel.sendParticles(ParticleTypes.FLAME,
                center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5,
                20, 1.5, 1.5, 1.5, 0.05);

        serverLevel.sendParticles(ParticleTypes.SMOKE,
                center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5,
                30, 1.5, 1.5, 1.5, 0.02);

        return true;
    }
}
