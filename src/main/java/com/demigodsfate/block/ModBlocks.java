package com.demigodsfate.block;

import com.demigodsfate.DemigodsFate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DemigodsFate.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(DemigodsFate.MODID);

    // Celestial Bronze Block — decorative/storage
    public static final DeferredBlock<Block> CELESTIAL_BRONZE_BLOCK = BLOCKS.registerSimpleBlock(
            "celestial_bronze_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(5.0f, 6.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops());

    public static final DeferredItem<BlockItem> CELESTIAL_BRONZE_BLOCK_ITEM = BLOCK_ITEMS.registerSimpleBlockItem(
            "celestial_bronze_block", CELESTIAL_BRONZE_BLOCK);

    // Imperial Gold Block — decorative/storage
    public static final DeferredBlock<Block> IMPERIAL_GOLD_BLOCK = BLOCKS.registerSimpleBlock(
            "imperial_gold_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(5.0f, 6.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops());

    public static final DeferredItem<BlockItem> IMPERIAL_GOLD_BLOCK_ITEM = BLOCK_ITEMS.registerSimpleBlockItem(
            "imperial_gold_block", IMPERIAL_GOLD_BLOCK);

    // Oracle Seat — interactable block for receiving prophecies
    public static final DeferredBlock<Block> ORACLE_SEAT = BLOCKS.registerSimpleBlock(
            "oracle_seat",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion());

    public static final DeferredItem<BlockItem> ORACLE_SEAT_ITEM = BLOCK_ITEMS.registerSimpleBlockItem(
            "oracle_seat", ORACLE_SEAT);
}
