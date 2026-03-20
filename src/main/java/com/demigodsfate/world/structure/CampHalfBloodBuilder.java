package com.demigodsfate.world.structure;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.ModEntities;
import com.demigodsfate.entity.npc.ChironEntity;
import com.demigodsfate.entity.npc.MrDEntity;
import com.demigodsfate.world.CampSafeZone;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * Programmatically generates Camp Half-Blood as described in the Percy Jackson series.
 * 12 distinct cabins arranged in an omega/U shape around a central courtyard,
 * a Big House, and an amphitheater campfire.
 */
public class CampHalfBloodBuilder {

    private static final int CAMP_RADIUS = 80;
    private static final int CLEARING_RADIUS = 70;

    // Cabin dimensions: 7 wide (X), 5 deep (Z), 4 tall (Y)
    private static final int CABIN_W = 7;
    private static final int CABIN_D = 5;
    private static final int CABIN_H = 4;

    /**
     * Builds Camp Half-Blood centered at the given position.
     */
    public static void buildCamp(ServerLevel level, BlockPos center) {
        DemigodsFate.LOGGER.info("Building Camp Half-Blood at {}", center);

        // Step 1: Flatten and create a grass clearing
        createClearing(level, center);

        // Step 2: Build the 12 cabins in omega/U shape
        buildAllCabins(level, center);

        // Step 3: Build the Big House
        buildBigHouse(level, center.offset(-30, 0, -20));

        // Step 4: Build the amphitheater campfire
        buildCampfire(level, center);

        // Step 5: Spawn NPCs at the Big House
        spawnNpcs(level, center.offset(-30, 0, -20));

        // Step 6: Register safe zone
        CampSafeZone.addSafeZone(center, CAMP_RADIUS, "Camp Half-Blood");

        DemigodsFate.LOGGER.info("Camp Half-Blood construction complete");
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
                // Clear above ground (5 blocks up)
                for (int y = 0; y < 10; y++) {
                    level.setBlock(center.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    // ========================================================================
    // Cabin Layout - Omega/U Shape
    // ========================================================================

    private static void buildAllCabins(ServerLevel level, BlockPos center) {
        // Cabins arranged in a U/omega shape opening to the south.
        // Left arm (west side, going north): cabins at increasing Z offsets
        // Top arc (north side, going east): cabins at increasing X offsets
        // Right arm (east side, going south): cabins at decreasing Z offsets

        // Left arm (west) - 4 cabins facing east
        buildCabinZeus(level,    center.offset(-25, 0, -20));  // Cabin 1
        buildCabinAres(level,    center.offset(-25, 0, -10));  // Cabin 5
        buildCabinHephaestus(level, center.offset(-25, 0, 0)); // Cabin 9
        buildCabinHades(level,   center.offset(-25, 0, 10));   // Cabin 13

        // Top arc (north) - 4 cabins facing south
        buildCabinPoseidon(level, center.offset(-12, 0, -25)); // Cabin 3
        buildCabinAthena(level,   center.offset(-2, 0, -25));  // Cabin 6
        buildCabinApollo(level,   center.offset(8, 0, -25));   // Cabin 7
        buildCabinGeneric1(level, center.offset(18, 0, -25));  // Cabin 2 (Hera - generic/honorary)

        // Right arm (east) - 4 cabins facing west
        buildCabinAphrodite(level, center.offset(25, 0, -20)); // Cabin 10
        buildCabinHermes(level,    center.offset(25, 0, -10)); // Cabin 11
        buildCabinDionysus(level,  center.offset(25, 0, 0));   // Cabin 12
        buildCabinGeneric2(level,  center.offset(25, 0, 10));  // Cabin 14 (Iris - generic)
    }

    // ========================================================================
    // Cabin 1: Zeus - White concrete, gold accents
    // ========================================================================

    private static void buildCabinZeus(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.WHITE_CONCRETE.defaultBlockState(),
                Blocks.WHITE_CONCRETE.defaultBlockState());
        // Gold accent trim along the top edge
        for (int x = 0; x < CABIN_W; x++) {
            level.setBlock(origin.offset(x, CABIN_H - 1, 0), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
            level.setBlock(origin.offset(x, CABIN_H - 1, CABIN_D - 1), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        }
        // Lightning rod on roof
        level.setBlock(origin.offset(3, CABIN_H, 2), Blocks.LIGHTNING_ROD.defaultBlockState(), 3);
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 3: Poseidon - Stone, prismarine
    // ========================================================================

    private static void buildCabinPoseidon(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.STONE.defaultBlockState(),
                Blocks.PRISMARINE.defaultBlockState());
        // Prismarine accents on corners
        for (int y = 0; y < CABIN_H; y++) {
            level.setBlock(origin.offset(0, y, 0), Blocks.PRISMARINE_BRICKS.defaultBlockState(), 3);
            level.setBlock(origin.offset(CABIN_W - 1, y, 0), Blocks.PRISMARINE_BRICKS.defaultBlockState(), 3);
            level.setBlock(origin.offset(0, y, CABIN_D - 1), Blocks.PRISMARINE_BRICKS.defaultBlockState(), 3);
            level.setBlock(origin.offset(CABIN_W - 1, y, CABIN_D - 1), Blocks.PRISMARINE_BRICKS.defaultBlockState(), 3);
        }
        // Sea lantern inside
        level.setBlock(origin.offset(5, 2, 3), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 5: Ares - Red concrete, nether brick fence (barbed wire)
    // ========================================================================

    private static void buildCabinAres(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.RED_CONCRETE.defaultBlockState(),
                Blocks.RED_CONCRETE.defaultBlockState());
        // Nether brick fence "barbed wire" along roofline
        for (int x = 0; x < CABIN_W; x++) {
            level.setBlock(origin.offset(x, CABIN_H, 0), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), 3);
            level.setBlock(origin.offset(x, CABIN_H, CABIN_D - 1), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), 3);
        }
        for (int z = 0; z < CABIN_D; z++) {
            level.setBlock(origin.offset(0, CABIN_H, z), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), 3);
            level.setBlock(origin.offset(CABIN_W - 1, CABIN_H, z), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), 3);
        }
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 6: Athena - Gray concrete, bookshelves inside
    // ========================================================================

    private static void buildCabinAthena(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.GRAY_CONCRETE.defaultBlockState(),
                Blocks.GRAY_CONCRETE.defaultBlockState());
        // Bookshelves along the back wall
        for (int x = 1; x < CABIN_W - 1; x++) {
            level.setBlock(origin.offset(x, 1, CABIN_D - 2), Blocks.BOOKSHELF.defaultBlockState(), 3);
            level.setBlock(origin.offset(x, 2, CABIN_D - 2), Blocks.BOOKSHELF.defaultBlockState(), 3);
        }
        // Crafting table (study desk)
        level.setBlock(origin.offset(1, 1, 1), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        placeBed(level, origin.offset(5, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 7: Apollo - Gold blocks, glowstone
    // ========================================================================

    private static void buildCabinApollo(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.GOLD_BLOCK.defaultBlockState(),
                Blocks.GOLD_BLOCK.defaultBlockState());
        // Glowstone lighting on ceiling
        level.setBlock(origin.offset(2, CABIN_H - 1, 2), Blocks.GLOWSTONE.defaultBlockState(), 3);
        level.setBlock(origin.offset(4, CABIN_H - 1, 2), Blocks.GLOWSTONE.defaultBlockState(), 3);
        // Glowstone accents on floor corners
        level.setBlock(origin.offset(1, 1, 1), Blocks.GLOWSTONE.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 1, 3), Blocks.GLOWSTONE.defaultBlockState(), 3);
        placeBed(level, origin.offset(1, 1, 3), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 9: Hephaestus - Bricks, iron blocks, furnaces
    // ========================================================================

    private static void buildCabinHephaestus(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.BRICKS.defaultBlockState(),
                Blocks.IRON_BLOCK.defaultBlockState());
        // Furnaces along back wall
        for (int x = 1; x < CABIN_W - 1; x++) {
            level.setBlock(origin.offset(x, 1, CABIN_D - 2), Blocks.FURNACE.defaultBlockState(), 3);
        }
        // Anvil
        level.setBlock(origin.offset(3, 1, 2), Blocks.ANVIL.defaultBlockState(), 3);
        // Smokestack (chimney)
        for (int y = CABIN_H; y < CABIN_H + 3; y++) {
            level.setBlock(origin.offset(1, y, 1), Blocks.BRICKS.defaultBlockState(), 3);
        }
        level.setBlock(origin.offset(1, CABIN_H + 3, 1), Blocks.CAMPFIRE.defaultBlockState(), 3);
        placeBed(level, origin.offset(5, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 10: Aphrodite - Pink terracotta, flowers
    // ========================================================================

    private static void buildCabinAphrodite(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.PINK_TERRACOTTA.defaultBlockState(),
                Blocks.WHITE_TERRACOTTA.defaultBlockState());
        // Flowers outside the entrance
        level.setBlock(origin.offset(2, 1, -1), Blocks.ROSE_BUSH.defaultBlockState(), 3);
        level.setBlock(origin.offset(4, 1, -1), Blocks.PEONY.defaultBlockState(), 3);
        // Flowers inside
        level.setBlock(origin.offset(1, 1, 1), Blocks.POTTED_PINK_TULIP.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 1, 1), Blocks.POTTED_ALLIUM.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 1, 3), Blocks.POTTED_LILY_OF_THE_VALLEY.defaultBlockState(), 3);
        placeBed(level, origin.offset(1, 1, 3), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 11: Hermes - Oak wood (plain)
    // ========================================================================

    private static void buildCabinHermes(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.OAK_PLANKS.defaultBlockState(),
                Blocks.OAK_LOG.defaultBlockState());
        // Plain interior - just some chests (Hermes = travelers, lots of stuff)
        level.setBlock(origin.offset(1, 1, 3), Blocks.CHEST.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 1, 3), Blocks.CHEST.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 1, 1), Blocks.CHEST.defaultBlockState(), 3);
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 12: Dionysus - Oak wood with vines
    // ========================================================================

    private static void buildCabinDionysus(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.OAK_PLANKS.defaultBlockState(),
                Blocks.OAK_LOG.defaultBlockState());
        // Vines covering the exterior walls
        for (int y = 1; y < CABIN_H; y++) {
            for (int x = 0; x < CABIN_W; x++) {
                // Front wall vines (skip door)
                if (x != 3 && x != 3) {
                    placeVineOnFace(level, origin.offset(x, y, -1), Direction.SOUTH);
                }
                // Back wall vines
                placeVineOnFace(level, origin.offset(x, y, CABIN_D), Direction.NORTH);
            }
            for (int z = 0; z < CABIN_D; z++) {
                placeVineOnFace(level, origin.offset(-1, y, z), Direction.EAST);
                placeVineOnFace(level, origin.offset(CABIN_W, y, z), Direction.WEST);
            }
        }
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Cabin 13: Hades - Obsidian, soul lanterns
    // ========================================================================

    private static void buildCabinHades(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.OBSIDIAN.defaultBlockState(),
                Blocks.CRYING_OBSIDIAN.defaultBlockState());
        // Soul lanterns for eerie green lighting
        level.setBlock(origin.offset(1, 3, 1), Blocks.SOUL_LANTERN.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 3, 1), Blocks.SOUL_LANTERN.defaultBlockState(), 3);
        level.setBlock(origin.offset(1, 3, 3), Blocks.SOUL_LANTERN.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 3, 3), Blocks.SOUL_LANTERN.defaultBlockState(), 3);
        // Soul soil floor
        for (int x = 1; x < CABIN_W - 1; x++) {
            for (int z = 1; z < CABIN_D - 1; z++) {
                level.setBlock(origin.offset(x, 0, z), Blocks.SOUL_SOIL.defaultBlockState(), 3);
            }
        }
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Generic Cabin 1 (Hera - honorary, marble columns)
    // ========================================================================

    private static void buildCabinGeneric1(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.QUARTZ_BLOCK.defaultBlockState(),
                Blocks.QUARTZ_PILLAR.defaultBlockState());
        // Quartz pillar columns at corners inside
        for (int y = 1; y < CABIN_H - 1; y++) {
            level.setBlock(origin.offset(1, y, 1), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(5, y, 1), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(1, y, 3), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
            level.setBlock(origin.offset(5, y, 3), Blocks.QUARTZ_PILLAR.defaultBlockState(), 3);
        }
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Generic Cabin 2 (Iris - rainbow accents)
    // ========================================================================

    private static void buildCabinGeneric2(ServerLevel level, BlockPos origin) {
        buildCabinShell(level, origin,
                Blocks.WHITE_CONCRETE.defaultBlockState(),
                Blocks.WHITE_CONCRETE.defaultBlockState());
        // Rainbow stripe along top of front wall
        BlockState[] rainbow = {
                Blocks.RED_CONCRETE.defaultBlockState(),
                Blocks.ORANGE_CONCRETE.defaultBlockState(),
                Blocks.YELLOW_CONCRETE.defaultBlockState(),
                Blocks.LIME_CONCRETE.defaultBlockState(),
                Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState(),
                Blocks.BLUE_CONCRETE.defaultBlockState(),
                Blocks.PURPLE_CONCRETE.defaultBlockState()
        };
        for (int x = 0; x < CABIN_W; x++) {
            level.setBlock(origin.offset(x, CABIN_H - 1, 0), rainbow[x % rainbow.length], 3);
        }
        placeBed(level, origin.offset(1, 1, 1), Direction.SOUTH);
        placeDoor(level, origin.offset(3, 1, 0), Direction.SOUTH);
    }

    // ========================================================================
    // Big House - 12x8x5, Chiron's headquarters
    // ========================================================================

    private static void buildBigHouse(ServerLevel level, BlockPos origin) {
        int w = 12, d = 8, h = 5;

        // Floor
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(x, 0, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }

        // Walls
        for (int y = 1; y < h; y++) {
            for (int x = 0; x < w; x++) {
                level.setBlock(origin.offset(x, y, 0), Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(), 3);
                level.setBlock(origin.offset(x, y, d - 1), Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(), 3);
            }
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(0, y, z), Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(), 3);
                level.setBlock(origin.offset(w - 1, y, z), Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(), 3);
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

        // Roof (dark oak slabs)
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                level.setBlock(origin.offset(x, h, z), Blocks.DARK_OAK_PLANKS.defaultBlockState(), 3);
            }
        }

        // Door
        placeDoor(level, origin.offset(6, 1, 0), Direction.SOUTH);

        // Windows (glass panes on side walls)
        for (int x = 2; x < w - 2; x += 3) {
            level.setBlock(origin.offset(x, 2, 0), Blocks.GLASS_PANE.defaultBlockState(), 3);
            level.setBlock(origin.offset(x, 3, 0), Blocks.GLASS_PANE.defaultBlockState(), 3);
            level.setBlock(origin.offset(x, 2, d - 1), Blocks.GLASS_PANE.defaultBlockState(), 3);
            level.setBlock(origin.offset(x, 3, d - 1), Blocks.GLASS_PANE.defaultBlockState(), 3);
        }

        // Table/chair area (meeting room) - oak stairs as chairs, crafting table as table
        level.setBlock(origin.offset(3, 1, 4), Blocks.OAK_STAIRS.defaultBlockState(), 3);
        level.setBlock(origin.offset(4, 1, 4), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        level.setBlock(origin.offset(5, 1, 4), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        level.setBlock(origin.offset(6, 1, 4), Blocks.OAK_STAIRS.defaultBlockState(), 3);
        level.setBlock(origin.offset(3, 1, 5), Blocks.OAK_STAIRS.defaultBlockState(), 3);
        level.setBlock(origin.offset(6, 1, 5), Blocks.OAK_STAIRS.defaultBlockState(), 3);

        // Lanterns for light
        level.setBlock(origin.offset(3, 4, 3), Blocks.LANTERN.defaultBlockState(), 3);
        level.setBlock(origin.offset(8, 4, 3), Blocks.LANTERN.defaultBlockState(), 3);

        // Bookshelf wall (Chiron's study)
        for (int z = 1; z < d - 1; z++) {
            level.setBlock(origin.offset(1, 1, z), Blocks.BOOKSHELF.defaultBlockState(), 3);
            level.setBlock(origin.offset(1, 2, z), Blocks.BOOKSHELF.defaultBlockState(), 3);
        }
    }

    // ========================================================================
    // Campfire / Amphitheater
    // ========================================================================

    private static void buildCampfire(ServerLevel level, BlockPos center) {
        // Stone ring around the campfire
        int radius = 4;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (dist >= radius - 0.5 && dist <= radius + 0.5) {
                    level.setBlock(center.offset(x, 0, z), Blocks.STONE_BRICKS.defaultBlockState(), 3);
                }
            }
        }
        // Inner ring seats (oak stairs)
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (dist >= 1.5 && dist <= 2.5) {
                    level.setBlock(center.offset(x, 0, z), Blocks.OAK_STAIRS.defaultBlockState(), 3);
                }
            }
        }
        // Central fire
        level.setBlock(center, Blocks.CAMPFIRE.defaultBlockState(), 3);
        // Netherrack under for perpetual fire look
        level.setBlock(center.below(), Blocks.NETHERRACK.defaultBlockState(), 3);
    }

    // ========================================================================
    // NPC Spawning
    // ========================================================================

    private static void spawnNpcs(ServerLevel level, BlockPos bigHouseOrigin) {
        // Spawn Chiron inside the Big House
        ChironEntity chiron = ModEntities.CHIRON.get().create(level);
        if (chiron != null) {
            chiron.moveTo(bigHouseOrigin.getX() + 4, bigHouseOrigin.getY() + 1, bigHouseOrigin.getZ() + 3);
            chiron.setCustomName(Component.literal("Chiron").withStyle(ChatFormatting.GOLD));
            chiron.setCustomNameVisible(true);
            level.addFreshEntity(chiron);
        }

        // Spawn Mr. D on the porch
        MrDEntity mrD = ModEntities.MR_D.get().create(level);
        if (mrD != null) {
            mrD.moveTo(bigHouseOrigin.getX() + 8, bigHouseOrigin.getY() + 1, bigHouseOrigin.getZ() + 3);
            mrD.setCustomName(Component.literal("Mr. D").withStyle(ChatFormatting.DARK_PURPLE));
            mrD.setCustomNameVisible(true);
            level.addFreshEntity(mrD);
        }
    }

    // ========================================================================
    // Utility: Generic Cabin Shell
    // ========================================================================

    /**
     * Builds the basic shell of a 7x5x4 cabin: floor, walls, interior air, and flat roof.
     *
     * @param wallBlock  the primary wall material
     * @param accentBlock used for corners/edges
     */
    private static void buildCabinShell(ServerLevel level, BlockPos origin,
                                        BlockState wallBlock, BlockState accentBlock) {
        // Floor
        for (int x = 0; x < CABIN_W; x++) {
            for (int z = 0; z < CABIN_D; z++) {
                level.setBlock(origin.offset(x, 0, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }

        // Walls
        for (int y = 1; y < CABIN_H; y++) {
            for (int x = 0; x < CABIN_W; x++) {
                level.setBlock(origin.offset(x, y, 0), wallBlock, 3);
                level.setBlock(origin.offset(x, y, CABIN_D - 1), wallBlock, 3);
            }
            for (int z = 0; z < CABIN_D; z++) {
                level.setBlock(origin.offset(0, y, z), wallBlock, 3);
                level.setBlock(origin.offset(CABIN_W - 1, y, z), wallBlock, 3);
            }
            // Accent on corners
            level.setBlock(origin.offset(0, y, 0), accentBlock, 3);
            level.setBlock(origin.offset(CABIN_W - 1, y, 0), accentBlock, 3);
            level.setBlock(origin.offset(0, y, CABIN_D - 1), accentBlock, 3);
            level.setBlock(origin.offset(CABIN_W - 1, y, CABIN_D - 1), accentBlock, 3);
        }

        // Interior air
        for (int y = 1; y < CABIN_H; y++) {
            for (int x = 1; x < CABIN_W - 1; x++) {
                for (int z = 1; z < CABIN_D - 1; z++) {
                    level.setBlock(origin.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        // Flat roof
        for (int x = 0; x < CABIN_W; x++) {
            for (int z = 0; z < CABIN_D; z++) {
                level.setBlock(origin.offset(x, CABIN_H, z), Blocks.OAK_SLAB.defaultBlockState(), 3);
            }
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

    // ========================================================================
    // Utility: Place a Bed
    // ========================================================================

    private static void placeBed(ServerLevel level, BlockPos headPos, Direction facing) {
        BlockState foot = Blocks.RED_BED.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                .setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
        BlockState head = Blocks.RED_BED.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                .setValue(BlockStateProperties.BED_PART, BedPart.HEAD);
        level.setBlock(headPos, foot, 3);
        level.setBlock(headPos.relative(facing), head, 3);
    }

    // ========================================================================
    // Utility: Place a Vine on a block face
    // ========================================================================

    private static void placeVineOnFace(ServerLevel level, BlockPos pos, Direction face) {
        BlockState vine = Blocks.VINE.defaultBlockState();
        // Vine block state uses directional boolean properties
        try {
            vine = vine.setValue(BlockStateProperties.UP, false);
            switch (face) {
                case NORTH -> vine = vine.setValue(BlockStateProperties.NORTH, true);
                case SOUTH -> vine = vine.setValue(BlockStateProperties.SOUTH, true);
                case EAST -> vine = vine.setValue(BlockStateProperties.EAST, true);
                case WEST -> vine = vine.setValue(BlockStateProperties.WEST, true);
                default -> { return; }
            }
            level.setBlock(pos, vine, 3);
        } catch (Exception e) {
            // Silently skip if vine placement fails
        }
    }
}
