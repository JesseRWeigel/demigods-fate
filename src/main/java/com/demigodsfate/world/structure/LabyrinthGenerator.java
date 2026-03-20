package com.demigodsfate.world.structure;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

/**
 * Procedural Labyrinth generator — creates a maze of interconnected rooms.
 * Layout changes every time a new labyrinth is generated.
 *
 * Room types:
 * - Empty corridor
 * - Monster arena (spawns hostile mobs)
 * - Treasure vault (chests with loot)
 * - Trap room (pressure plates + TNT / lava)
 * - Dead end (cobwebs, bones)
 * - Daedalus's Workshop (special room with rare loot)
 */
public class LabyrinthGenerator {
    private static final int ROOM_SIZE = 9;       // Each room is 9x9
    private static final int ROOM_HEIGHT = 5;     // 5 blocks tall
    private static final int WALL_THICKNESS = 1;
    private static final BlockState FLOOR = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
    private static final BlockState WALL = Blocks.DEEPSLATE_TILES.defaultBlockState();
    private static final BlockState CEILING = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    /**
     * Generate a labyrinth at the given position.
     * @param gridSize Number of rooms in each direction (e.g., 8 = 8x8 grid)
     */
    public static void generate(ServerLevel level, BlockPos entrance, int gridSize, long seed) {
        Random random = new Random(seed);
        boolean[][] visited = new boolean[gridSize][gridSize];
        boolean[][][] connections = new boolean[gridSize][gridSize][4]; // N, S, E, W

        // Generate maze using recursive backtracker
        generateMaze(visited, connections, 0, 0, gridSize, random);

        // Assign room types
        RoomType[][] roomTypes = new RoomType[gridSize][gridSize];
        assignRoomTypes(roomTypes, gridSize, random);

        // Place Daedalus's Workshop in the furthest room from entrance
        roomTypes[gridSize - 1][gridSize - 1] = RoomType.WORKSHOP;

        // Build the physical structure
        for (int gx = 0; gx < gridSize; gx++) {
            for (int gz = 0; gz < gridSize; gz++) {
                BlockPos roomPos = entrance.offset(gx * ROOM_SIZE, 0, gz * ROOM_SIZE);
                buildRoom(level, roomPos, roomTypes[gx][gz], connections[gx][gz], random);
            }
        }

        // Add entrance marker
        level.setBlock(entrance.offset(ROOM_SIZE / 2, 0, -1), Blocks.GLOWSTONE.defaultBlockState(), 3);
        level.setBlock(entrance.offset(ROOM_SIZE / 2, 1, -1), Blocks.GLOWSTONE.defaultBlockState(), 3);

        DemigodsFate.LOGGER.info("Generated {}x{} labyrinth at {}", gridSize, gridSize, entrance);
    }

    private static void generateMaze(boolean[][] visited, boolean[][][] connections,
                                      int x, int z, int size, Random random) {
        visited[x][z] = true;
        int[] dirs = {0, 1, 2, 3}; // N, S, E, W
        shuffleArray(dirs, random);

        for (int dir : dirs) {
            int nx = x + (dir == 2 ? 1 : dir == 3 ? -1 : 0);
            int nz = z + (dir == 0 ? -1 : dir == 1 ? 1 : 0);

            if (nx >= 0 && nx < size && nz >= 0 && nz < size && !visited[nx][nz]) {
                connections[x][z][dir] = true;
                connections[nx][nz][opposite(dir)] = true;
                generateMaze(visited, connections, nx, nz, size, random);
            }
        }
    }

    private static void assignRoomTypes(RoomType[][] types, int size, Random random) {
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                float roll = random.nextFloat();
                if (x == 0 && z == 0) {
                    types[x][z] = RoomType.EMPTY; // Entrance is always empty
                } else if (roll < 0.25f) {
                    types[x][z] = RoomType.MONSTER;
                } else if (roll < 0.40f) {
                    types[x][z] = RoomType.TREASURE;
                } else if (roll < 0.55f) {
                    types[x][z] = RoomType.TRAP;
                } else if (roll < 0.70f) {
                    types[x][z] = RoomType.DEAD_END;
                } else {
                    types[x][z] = RoomType.EMPTY;
                }
            }
        }
    }

    private static void buildRoom(ServerLevel level, BlockPos pos, RoomType type,
                                   boolean[] connections, Random random) {
        // Clear the room
        for (int x = 0; x < ROOM_SIZE; x++) {
            for (int z = 0; z < ROOM_SIZE; z++) {
                // Floor
                level.setBlock(pos.offset(x, 0, z), FLOOR, 2);
                // Ceiling
                level.setBlock(pos.offset(x, ROOM_HEIGHT, z), CEILING, 2);
                // Air inside
                for (int y = 1; y < ROOM_HEIGHT; y++) {
                    level.setBlock(pos.offset(x, y, z), AIR, 2);
                }
            }
        }

        // Walls with openings for connections
        for (int i = 0; i < ROOM_SIZE; i++) {
            for (int y = 1; y < ROOM_HEIGHT; y++) {
                // North wall (z=0)
                if (!connections[0] || i < 3 || i > 5)
                    level.setBlock(pos.offset(i, y, 0), WALL, 2);
                // South wall (z=ROOM_SIZE-1)
                if (!connections[1] || i < 3 || i > 5)
                    level.setBlock(pos.offset(i, y, ROOM_SIZE - 1), WALL, 2);
                // East wall (x=ROOM_SIZE-1)
                if (!connections[2] || i < 3 || i > 5)
                    level.setBlock(pos.offset(ROOM_SIZE - 1, y, i), WALL, 2);
                // West wall (x=0)
                if (!connections[3] || i < 3 || i > 5)
                    level.setBlock(pos.offset(0, y, i), WALL, 2);
            }
        }

        // Add a torch for minimal lighting
        level.setBlock(pos.offset(ROOM_SIZE / 2, 2, ROOM_SIZE / 2), Blocks.TORCH.defaultBlockState(), 3);

        // Decorate based on room type
        switch (type) {
            case MONSTER -> decorateMonster(level, pos, random);
            case TREASURE -> decorateTreasure(level, pos, random);
            case TRAP -> decorateTrap(level, pos, random);
            case DEAD_END -> decorateDeadEnd(level, pos, random);
            case WORKSHOP -> decorateWorkshop(level, pos, random);
            default -> {} // Empty room
        }
    }

    private static void decorateMonster(ServerLevel level, BlockPos pos, Random random) {
        // Replace torch with soul lantern
        level.setBlock(pos.offset(ROOM_SIZE / 2, 2, ROOM_SIZE / 2), Blocks.SOUL_LANTERN.defaultBlockState(), 3);
        // Add spawner-like redstone lamps
        level.setBlock(pos.offset(2, 1, 2), Blocks.REDSTONE_LAMP.defaultBlockState(), 3);
        level.setBlock(pos.offset(6, 1, 6), Blocks.REDSTONE_LAMP.defaultBlockState(), 3);
    }

    private static void decorateTreasure(ServerLevel level, BlockPos pos, Random random) {
        // Place chests
        level.setBlock(pos.offset(ROOM_SIZE / 2, 1, ROOM_SIZE / 2), Blocks.CHEST.defaultBlockState(), 3);
        // Gold block accents
        level.setBlock(pos.offset(2, 1, 2), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        level.setBlock(pos.offset(6, 1, 6), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        // Glowstone lighting
        level.setBlock(pos.offset(ROOM_SIZE / 2, 2, ROOM_SIZE / 2), Blocks.GLOWSTONE.defaultBlockState(), 3);
    }

    private static void decorateTrap(ServerLevel level, BlockPos pos, Random random) {
        // Pressure plates over TNT
        for (int x = 3; x <= 5; x++) {
            for (int z = 3; z <= 5; z++) {
                level.setBlock(pos.offset(x, 0, z), Blocks.TNT.defaultBlockState(), 2);
                level.setBlock(pos.offset(x, 1, z), Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 3);
            }
        }
        // Lava in corners
        level.setBlock(pos.offset(1, 1, 1), Blocks.LAVA.defaultBlockState(), 3);
        level.setBlock(pos.offset(7, 1, 7), Blocks.LAVA.defaultBlockState(), 3);
    }

    private static void decorateDeadEnd(ServerLevel level, BlockPos pos, Random random) {
        // Cobwebs and bones
        for (int i = 0; i < 5; i++) {
            int x = 1 + random.nextInt(7);
            int z = 1 + random.nextInt(7);
            level.setBlock(pos.offset(x, 1, z), Blocks.COBWEB.defaultBlockState(), 3);
        }
        level.setBlock(pos.offset(4, 1, 4), Blocks.SKELETON_SKULL.defaultBlockState(), 3);
    }

    private static void decorateWorkshop(ServerLevel level, BlockPos pos, Random random) {
        // Daedalus's Workshop — special loot room
        // Workbenches and furnaces
        level.setBlock(pos.offset(2, 1, 2), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        level.setBlock(pos.offset(3, 1, 2), Blocks.SMITHING_TABLE.defaultBlockState(), 3);
        level.setBlock(pos.offset(4, 1, 2), Blocks.ANVIL.defaultBlockState(), 3);
        level.setBlock(pos.offset(6, 1, 2), Blocks.BLAST_FURNACE.defaultBlockState(), 3);
        // Enchanting setup
        level.setBlock(pos.offset(4, 1, 6), Blocks.ENCHANTING_TABLE.defaultBlockState(), 3);
        level.setBlock(pos.offset(3, 1, 6), Blocks.BOOKSHELF.defaultBlockState(), 3);
        level.setBlock(pos.offset(5, 1, 6), Blocks.BOOKSHELF.defaultBlockState(), 3);
        // Treasure chest
        level.setBlock(pos.offset(4, 1, 4), Blocks.CHEST.defaultBlockState(), 3);
        // Diamond block as floor accent (Daedalus's treasures)
        level.setBlock(pos.offset(4, 0, 4), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
        // Bright lighting
        level.setBlock(pos.offset(2, 3, 4), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(pos.offset(6, 3, 4), Blocks.SEA_LANTERN.defaultBlockState(), 3);
    }

    private static int opposite(int dir) {
        return switch (dir) {
            case 0 -> 1; // N -> S
            case 1 -> 0; // S -> N
            case 2 -> 3; // E -> W
            case 3 -> 2; // W -> E
            default -> 0;
        };
    }

    private static void shuffleArray(int[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    enum RoomType {
        EMPTY, MONSTER, TREASURE, TRAP, DEAD_END, WORKSHOP
    }
}
