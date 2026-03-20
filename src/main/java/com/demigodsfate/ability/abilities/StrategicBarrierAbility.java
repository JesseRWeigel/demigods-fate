package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * Minerva Slot 0 — Place a 3-block-wide obsidian wall in front of you.
 */
public class StrategicBarrierAbility extends Ability {
    private static final int WALL_WIDTH = 3;
    private static final int WALL_HEIGHT = 3;
    private static final double DISTANCE = 3.0;

    public StrategicBarrierAbility() {
        super("strategic_barrier", "Strategic Barrier",  100, 0, GodParent.MINERVA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Calculate wall position in front of the player
        Vec3 lookVec = player.getLookAngle();
        Vec3 wallCenter = player.position().add(lookVec.x * DISTANCE, 0, lookVec.z * DISTANCE);
        BlockPos centerPos = BlockPos.containing(wallCenter);

        // Determine wall orientation (perpendicular to look direction)
        Direction facing = Direction.fromYRot(player.getYRot());
        Direction wallDir; // Direction the wall extends along
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            wallDir = Direction.EAST;
        } else {
            wallDir = Direction.SOUTH;
        }

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 0.8F);

        int blocksPlaced = 0;
        // Place obsidian wall
        for (int w = -(WALL_WIDTH / 2); w <= WALL_WIDTH / 2; w++) {
            for (int h = 0; h < WALL_HEIGHT; h++) {
                BlockPos wallPos = centerPos.relative(wallDir, w).above(h);
                // Only place in air/replaceable blocks
                if (level.getBlockState(wallPos).isAir() ||
                        level.getBlockState(wallPos).canBeReplaced()) {
                    level.setBlockAndUpdate(wallPos, Blocks.OBSIDIAN.defaultBlockState());
                    blocksPlaced++;

                    // Particles on each placed block
                    serverLevel.sendParticles(ParticleTypes.END_ROD,
                            wallPos.getX() + 0.5, wallPos.getY() + 0.5, wallPos.getZ() + 0.5,
                            3, 0.3, 0.3, 0.3, 0.02);
                }
            }
        }

        if (blocksPlaced == 0) return false;

        // Shimmer effect
        serverLevel.sendParticles(ParticleTypes.ENCHANT,
                centerPos.getX() + 0.5, centerPos.getY() + 1.5, centerPos.getZ() + 0.5,
                20, 1.0, 1.0, 1.0, 0.1);

        return true;
    }
}
