package com.demigodsfate.world.structure;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.world.CampSafeZone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * Programmatically generates Camp Jupiter with Roman-themed architecture.
 * Features 5 cohort barracks, Principia HQ, Senate House, Temple Hill,
 * and the Little Tiber river.
 */
public class CampJupiterBuilder {

    private static final int CAMP_RADIUS = 90;
    private static final int CLEARING_RADIUS = 80;

    /**
     * Builds Camp Jupiter centered at the given position.
     */
    public static void buildCamp(ServerLevel level, BlockPos center) {
        DemigodsFate.LOGGER.info("Building Camp Jupiter at {}", center);

        // Step 1: Flatten and create a clearing
        createClearing(level, center);

        // Step 2: Build the 5 cohort barracks
        buildCohortBarracks(level, center);

        // Step 3: Build the Principia (headquarters)
        buildPrincipia(level, center.offset(0, 0, -30));

        // Step 4: Build the Senate House
        buildSenateHouse(level, center.offset(25, 0, -25));

        // Step 5: Build Temple Hill
        buildTempleHill(level, center.offset(-30, 0, -35));

        // Step 6: Build the Little Tiber
        buildLittleTiber(level, center);

        // Step 7: Register safe zone
        CampSafeZone.addSafeZone(center, CAMP_RADIUS, "Camp Jupiter");

        DemigodsFate.LOGGER.info("Camp Jupiter construction complete");
    }

    // ========================================================================
    // Clearing
    // ========================================================================

    private static void createClearing(ServerLevel level, BlockPos center) {
        for (int x = -CLEARING_RADIUS; x <= CLEARING_RADIUS; x++) {
            for (int z = -CLEARING_RADIUS; z <= CLEARING_RADIUS; z++) {
                if (x * x + z * z > CLEARING_RADIUS * CLEARING_RADIUS) continue;
                BlockPos ground = center.offset(x, -1, z);
                level.setBlock(ground, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                for (int y = 0; y < 10; y++) {
                    level.setBlock(center.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    // ========================================================================
    // 5 Cohort Barracks - Deepslate brick, uniform military style
    // ========================================================================

    private static void buildCohortBarracks(ServerLevel level, BlockPos center) {
        // 5 barracks in a row along the east side
        for (int i = 0; i < 5; i++) {
            BlockPos origin = center.offset(15, 0, -10 + i * 12);
            buildBarrack(level, origin, i + 1);
        }
    }

    private static void buildBarrack(ServerLevel level, BlockPos origin, int cohortNumber) {
        int w = 10, d = 6, h = 4;

        // Floor - polished deepslate
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(x, 0, z), Blocks.POLISHED_DEEPSLATE.defaultBlockState(), 3);
            }
        }

        // Walls - deepslate bricks
        BlockState wall = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        for (int y = 1; y < h; y++) {
            for (int x = 0; x < w; x++) {
                level.setBlock(origin.offset(x, y, 0), wall, 3);
                level.setBlock(origin.offset(x, y, d - 1), wall, 3);
            }
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(0, y, z), wall, 3);
                level.setBlock(origin.offset(w - 1, y, z), wall, 3);
            }
        }

        // Interior air
        for (int y = 1; y < h; y++) {
            for (int x = 1; x < w - 1; x++) {
                for (int z = 1; z < d - 1; z++) {
                    level.setBlock(origin.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        // Roof - deepslate tiles
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(x, h, z), Blocks.DEEPSLATE_TILES.defaultBlockState(), 3);
            }
        }

        // Door
        placeDoor(level, origin.offset(5, 1, 0), Direction.SOUTH);

        // Interior: 4 beds in a row (military bunks)
        for (int i = 0; i < 4; i++) {
            BlockState bed = Blocks.RED_BED.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                    .setValue(BlockStateProperties.BED_PART,
                            net.minecraft.world.level.block.state.properties.BedPart.FOOT);
            BlockState bedHead = Blocks.RED_BED.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                    .setValue(BlockStateProperties.BED_PART,
                            net.minecraft.world.level.block.state.properties.BedPart.HEAD);
            level.setBlock(origin.offset(1 + i * 2, 1, 2), bed, 3);
            level.setBlock(origin.offset(2 + i * 2, 1, 2), bedHead, 3);
        }

        // Armor stand placeholder - use an iron block pedestal
        level.setBlock(origin.offset(1, 1, 4), Blocks.IRON_BLOCK.defaultBlockState(), 3);

        // Lantern
        level.setBlock(origin.offset(5, 3, 3), Blocks.LANTERN.defaultBlockState(), 3);

        // Cohort banner (use colored wool on top of fence)
        BlockState banner = switch (cohortNumber) {
            case 1 -> Blocks.RED_WOOL.defaultBlockState();
            case 2 -> Blocks.BLUE_WOOL.defaultBlockState();
            case 3 -> Blocks.GREEN_WOOL.defaultBlockState();
            case 4 -> Blocks.YELLOW_WOOL.defaultBlockState();
            case 5 -> Blocks.PURPLE_WOOL.defaultBlockState();
            default -> Blocks.WHITE_WOOL.defaultBlockState();
        };
        level.setBlock(origin.offset(5, h + 1, 0), Blocks.OAK_FENCE.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, h + 2, 0), banner, 3);
    }

    // ========================================================================
    // Principia - Headquarters, 14x10x6, quartz and deepslate
    // ========================================================================

    private static void buildPrincipia(ServerLevel level, BlockPos origin) {
        int w = 14, d = 10, h = 6;

        // Floor - polished deepslate
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(x, 0, z), Blocks.POLISHED_DEEPSLATE.defaultBlockState(), 3);
            }
        }

        // Walls - quartz with deepslate brick pillars at corners
        for (int y = 1; y < h; y++) {
            for (int x = 0; x < w; x++) {
                level.setBlock(origin.offset(x, y, 0), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
                level.setBlock(origin.offset(x, y, d - 1), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
            }
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(0, y, z), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
                level.setBlock(origin.offset(w - 1, y, z), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
            }
            // Deepslate brick pillars at corners
            level.setBlock(origin.offset(0, y, 0), Blocks.DEEPSLATE_BRICKS.defaultBlockState(), 3);
            level.setBlock(origin.offset(w - 1, y, 0), Blocks.DEEPSLATE_BRICKS.defaultBlockState(), 3);
            level.setBlock(origin.offset(0, y, d - 1), Blocks.DEEPSLATE_BRICKS.defaultBlockState(), 3);
            level.setBlock(origin.offset(w - 1, y, d - 1), Blocks.DEEPSLATE_BRICKS.defaultBlockState(), 3);
        }

        // Interior air
        for (int y = 1; y < h; y++) {
            for (int x = 1; x < w - 1; x++) {
                for (int z = 1; z < d - 1; z++) {
                    level.setBlock(origin.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        // Roof - deepslate tiles
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(x, h, z), Blocks.DEEPSLATE_TILES.defaultBlockState(), 3);
            }
        }

        // Grand entrance - double door width
        placeDoor(level, origin.offset(6, 1, 0), Direction.SOUTH);
        placeDoor(level, origin.offset(7, 1, 0), Direction.SOUTH);

        // Quartz pillar columns flanking the entrance (exterior)
        for (int y = 1; y <= h; y++) {
            level.setBlock(origin.offset(4, y, -1), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(9, y, -1), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
        }

        // Interior columns
        for (int y = 1; y < h; y++) {
            level.setBlock(origin.offset(3, y, 3), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(10, y, 3), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(3, y, 7), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(10, y, 7), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
        }

        // Praetor's desk
        level.setBlock(origin.offset(6, 1, 7), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        level.setBlock(origin.offset(7, 1, 7), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        level.setBlock(origin.offset(6, 1, 8), Blocks.OAK_STAIRS.defaultBlockState(), 3);
        level.setBlock(origin.offset(7, 1, 8), Blocks.OAK_STAIRS.defaultBlockState(), 3);

        // Eagle standard pedestal
        level.setBlock(origin.offset(7, 1, 5), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        level.setBlock(origin.offset(7, 2, 5), Blocks.LIGHTNING_ROD.defaultBlockState(), 3);

        // Lanterns
        level.setBlock(origin.offset(3, 5, 5), Blocks.LANTERN.defaultBlockState(), 3);
        level.setBlock(origin.offset(10, 5, 5), Blocks.LANTERN.defaultBlockState(), 3);

        // Windows
        for (int z = 2; z < d - 2; z += 2) {
            level.setBlock(origin.offset(0, 3, z), Blocks.GLASS_PANE.defaultBlockState(), 3);
            level.setBlock(origin.offset(w - 1, 3, z), Blocks.GLASS_PANE.defaultBlockState(), 3);
        }
    }

    // ========================================================================
    // Senate House - Circular-ish building with dome suggestion
    // ========================================================================

    private static void buildSenateHouse(ServerLevel level, BlockPos center) {
        int radius = 7;
        int h = 5;

        // Floor - polished deepslate circle
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    level.setBlock(center.offset(x, 0, z), Blocks.POLISHED_DEEPSLATE.defaultBlockState(), 3);
                }
            }
        }

        // Circular walls
        for (int y = 1; y < h; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + z * z;
                    // Wall ring
                    if (distSq <= radius * radius && distSq > (radius - 1) * (radius - 1)) {
                        level.setBlock(center.offset(x, y, z), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
                    }
                    // Interior air
                    if (distSq <= (radius - 1) * (radius - 1) && distSq > 0) {
                        level.setBlock(center.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        // Dome suggestion - stepped rings getting smaller
        for (int layer = 0; layer < 3; layer++) {
            int domeRadius = radius - layer - 1;
            for (int x = -domeRadius; x <= domeRadius; x++) {
                for (int z = -domeRadius; z <= domeRadius; z++) {
                    if (x * x + z * z <= domeRadius * domeRadius) {
                        level.setBlock(center.offset(x, h + layer, z),
                                Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
                    }
                }
            }
        }
        // Capstone
        level.setBlock(center.offset(0, h + 3, 0), Blocks.GOLD_BLOCK.defaultBlockState(), 3);

        // Interior air under dome
        for (int layer = 0; layer < 3; layer++) {
            int domeRadius = radius - layer - 2;
            if (domeRadius < 1) break;
            for (int x = -domeRadius; x <= domeRadius; x++) {
                for (int z = -domeRadius; z <= domeRadius; z++) {
                    if (x * x + z * z <= domeRadius * domeRadius) {
                        level.setBlock(center.offset(x, h + layer, z), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        // Entrance gap (south side)
        for (int y = 1; y <= 3; y++) {
            level.setBlock(center.offset(0, y, -radius), Blocks.AIR.defaultBlockState(), 3);
            level.setBlock(center.offset(1, y, -radius), Blocks.AIR.defaultBlockState(), 3);
        }

        // Interior: semi-circular seating (stairs facing center)
        for (int x = -4; x <= 4; x++) {
            for (int z = 1; z <= 4; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (dist >= 2.5 && dist <= 4.5) {
                    level.setBlock(center.offset(x, 1, z), Blocks.QUARTZ_STAIRS.defaultBlockState(), 3);
                }
            }
        }

        // Central speaking podium
        level.setBlock(center.offset(0, 1, 0), Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
        level.setBlock(center.offset(0, 2, 0), Blocks.LECTERN.defaultBlockState(), 3);

        // Lanterns
        level.setBlock(center.offset(-3, 4, 0), Blocks.LANTERN.defaultBlockState(), 3);
        level.setBlock(center.offset(3, 4, 0), Blocks.LANTERN.defaultBlockState(), 3);
    }

    // ========================================================================
    // Temple Hill - 3 small temples on a raised hill
    // ========================================================================

    private static void buildTempleHill(ServerLevel level, BlockPos center) {
        // Build the hill (raised platform)
        int hillRadius = 15;
        for (int x = -hillRadius; x <= hillRadius; x++) {
            for (int z = -hillRadius; z <= hillRadius; z++) {
                if (x * x + z * z <= hillRadius * hillRadius) {
                    // Tiered hill: 3 blocks high at center, tapering
                    double dist = Math.sqrt(x * x + z * z);
                    int height = (int) Math.max(1, 3 - dist / 6);
                    for (int y = 0; y < height; y++) {
                        level.setBlock(center.offset(x, y, z), Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                    }
                    // Clear above
                    for (int y = height; y < height + 8; y++) {
                        level.setBlock(center.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        // Temple of Jupiter (center, largest)
        buildTemple(level, center.offset(0, 3, 0), 7, 5,
                Blocks.QUARTZ_BLOCK.defaultBlockState(),
                Blocks.GOLD_BLOCK.defaultBlockState(), "Jupiter");

        // Temple of Mars (left)
        buildTemple(level, center.offset(-10, 2, 4), 5, 4,
                Blocks.RED_TERRACOTTA.defaultBlockState(),
                Blocks.IRON_BLOCK.defaultBlockState(), "Mars");

        // Temple of Pluto (right)
        buildTemple(level, center.offset(10, 2, 4), 5, 4,
                Blocks.DEEPSLATE_BRICKS.defaultBlockState(),
                Blocks.OBSIDIAN.defaultBlockState(), "Pluto");
    }

    private static void buildTemple(ServerLevel level, BlockPos origin, int size, int height,
                                    BlockState wallBlock, BlockState accentBlock, String name) {
        // Platform/floor
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                level.setBlock(origin.offset(x, 0, z), wallBlock, 3);
            }
        }

        // Four corner pillars
        for (int y = 1; y <= height; y++) {
            level.setBlock(origin.offset(0, y, 0), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(size - 1, y, 0), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(0, y, size - 1), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(size - 1, y, size - 1), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
        }

        // Roof
        for (int x = -1; x <= size; x++) {
            for (int z = -1; z <= size; z++) {
                level.setBlock(origin.offset(x, height + 1, z), wallBlock, 3);
            }
        }

        // Altar in center
        int mid = size / 2;
        level.setBlock(origin.offset(mid, 1, mid), accentBlock, 3);

        // Soul fire on altar (for atmosphere)
        level.setBlock(origin.offset(mid, 2, mid), Blocks.SOUL_CAMPFIRE.defaultBlockState(), 3);
    }

    // ========================================================================
    // Little Tiber - Water channel around the camp
    // ========================================================================

    private static void buildLittleTiber(ServerLevel level, BlockPos center) {
        // River runs east-west across the south side of camp
        int riverZ = 40;
        int riverLength = 70;
        int riverWidth = 3;

        for (int x = -riverLength / 2; x <= riverLength / 2; x++) {
            for (int w = 0; w < riverWidth; w++) {
                BlockPos pos = center.offset(x, -1, riverZ + w);
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                // Dig one block deeper for water to sit in
                level.setBlock(pos.below(), Blocks.CLAY.defaultBlockState(), 3);
            }
            // Stone brick banks
            level.setBlock(center.offset(x, -1, riverZ - 1), Blocks.STONE_BRICKS.defaultBlockState(), 3);
            level.setBlock(center.offset(x, -1, riverZ + riverWidth), Blocks.STONE_BRICKS.defaultBlockState(), 3);
        }

        // Simple bridge across the center
        for (int w = -1; w <= riverWidth; w++) {
            level.setBlock(center.offset(-2, 0, riverZ + w), Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3);
            level.setBlock(center.offset(-1, 0, riverZ + w), Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3);
            level.setBlock(center.offset(0, 0, riverZ + w), Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3);
            level.setBlock(center.offset(1, 0, riverZ + w), Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3);
            level.setBlock(center.offset(2, 0, riverZ + w), Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3);
        }
        // Bridge railings
        for (int x = -2; x <= 2; x++) {
            level.setBlock(center.offset(x, 1, riverZ - 1), Blocks.STONE_BRICK_WALL.defaultBlockState(), 3);
            level.setBlock(center.offset(x, 1, riverZ + riverWidth), Blocks.STONE_BRICK_WALL.defaultBlockState(), 3);
        }
    }

    // ========================================================================
    // Utility: Place a Door
    // ========================================================================

    private static void placeDoor(ServerLevel level, BlockPos pos, Direction facing) {
        BlockState lower = Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        BlockState upper = Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
        level.setBlock(pos, lower, 3);
        level.setBlock(pos.above(), upper, 3);
    }
}
