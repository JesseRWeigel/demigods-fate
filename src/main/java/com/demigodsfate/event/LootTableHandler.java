package com.demigodsfate.event;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;

/**
 * Injects Percy Jackson items into vanilla dungeon and temple loot tables.
 * Players can find drachmas, ambrosia, and celestial bronze in chests.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class LootTableHandler {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        var id = event.getName();

        // Add drachmas to most dungeon chests
        if (id.equals(BuiltInLootTables.SIMPLE_DUNGEON.location())
                || id.equals(BuiltInLootTables.ABANDONED_MINESHAFT.location())
                || id.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR.location())
                || id.equals(BuiltInLootTables.DESERT_PYRAMID.location())
                || id.equals(BuiltInLootTables.JUNGLE_TEMPLE.location())) {

            LootPool.Builder drachmaPool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(ModItems.GOLDEN_DRACHMA.get())
                            .setWeight(30)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                    .add(LootItem.lootTableItem(ModItems.AMBROSIA.get())
                            .setWeight(10)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                    .add(LootItem.lootTableItem(ModItems.CELESTIAL_BRONZE_INGOT.get())
                            .setWeight(15)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));

            event.getTable().addPool(drachmaPool.build());
        }

        // Add celestial bronze weapons to stronghold/end city chests
        if (id.equals(BuiltInLootTables.STRONGHOLD_LIBRARY.location())
                || id.equals(BuiltInLootTables.END_CITY_TREASURE.location())) {

            LootPool.Builder weaponPool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(ModItems.CELESTIAL_BRONZE_SWORD.get())
                            .setWeight(15))
                    .add(LootItem.lootTableItem(ModItems.IMPERIAL_GOLD_INGOT.get())
                            .setWeight(20)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                    .add(LootItem.lootTableItem(ModItems.NECTAR.get())
                            .setWeight(25)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

            event.getTable().addPool(weaponPool.build());
        }
    }
}
