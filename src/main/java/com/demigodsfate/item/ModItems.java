package com.demigodsfate.item;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.item.weapon.*;
import com.demigodsfate.item.consumable.AmbrosiaItem;
import com.demigodsfate.item.consumable.NectarItem;
import com.demigodsfate.item.consumable.GreekFireItem;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DemigodsFate.MODID);

    // --- Material Ingots ---
    public static final DeferredItem<Item> CELESTIAL_BRONZE_INGOT = ITEMS.registerSimpleItem(
            "celestial_bronze_ingot", new Item.Properties());
    public static final DeferredItem<Item> IMPERIAL_GOLD_INGOT = ITEMS.registerSimpleItem(
            "imperial_gold_ingot", new Item.Properties());

    // --- Celestial Bronze Tools ---
    public static final DeferredItem<SwordItem> CELESTIAL_BRONZE_SWORD = ITEMS.register(
            "celestial_bronze_sword",
            () -> new CelestialBronzeSwordItem(ModToolTiers.CELESTIAL_BRONZE, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.CELESTIAL_BRONZE, 3, -2.4f))));
    public static final DeferredItem<AxeItem> CELESTIAL_BRONZE_AXE = ITEMS.register(
            "celestial_bronze_axe",
            () -> new AxeItem(ModToolTiers.CELESTIAL_BRONZE, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.CELESTIAL_BRONZE, 6.0f, -3.1f))));
    public static final DeferredItem<PickaxeItem> CELESTIAL_BRONZE_PICKAXE = ITEMS.register(
            "celestial_bronze_pickaxe",
            () -> new PickaxeItem(ModToolTiers.CELESTIAL_BRONZE, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.CELESTIAL_BRONZE, 1, -2.8f))));
    public static final DeferredItem<ShovelItem> CELESTIAL_BRONZE_SHOVEL = ITEMS.register(
            "celestial_bronze_shovel",
            () -> new ShovelItem(ModToolTiers.CELESTIAL_BRONZE, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.CELESTIAL_BRONZE, 1.5f, -3.0f))));
    public static final DeferredItem<HoeItem> CELESTIAL_BRONZE_HOE = ITEMS.register(
            "celestial_bronze_hoe",
            () -> new HoeItem(ModToolTiers.CELESTIAL_BRONZE, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.CELESTIAL_BRONZE, -2, -1.0f))));

    // --- Imperial Gold Tools ---
    public static final DeferredItem<SwordItem> IMPERIAL_GOLD_SWORD = ITEMS.register(
            "imperial_gold_sword",
            () -> new ImperialGoldSwordItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, 3, -2.4f))));
    public static final DeferredItem<AxeItem> IMPERIAL_GOLD_AXE = ITEMS.register(
            "imperial_gold_axe",
            () -> new AxeItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, 5.0f, -3.0f))));
    public static final DeferredItem<PickaxeItem> IMPERIAL_GOLD_PICKAXE = ITEMS.register(
            "imperial_gold_pickaxe",
            () -> new PickaxeItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, 1, -2.8f))));
    public static final DeferredItem<ShovelItem> IMPERIAL_GOLD_SHOVEL = ITEMS.register(
            "imperial_gold_shovel",
            () -> new ShovelItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, 1.5f, -3.0f))));
    public static final DeferredItem<HoeItem> IMPERIAL_GOLD_HOE = ITEMS.register(
            "imperial_gold_hoe",
            () -> new HoeItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, -3, 0.0f))));

    // --- Divine Weapons ---
    public static final DeferredItem<SwordItem> RIPTIDE = ITEMS.register(
            "riptide",
            () -> new RiptideItem(ModToolTiers.CELESTIAL_BRONZE, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.CELESTIAL_BRONZE, 4, -2.2f))
                    .fireResistant()));

    public static final DeferredItem<SwordItem> IVLIVS = ITEMS.register(
            "ivlivs",
            () -> new IvlivsItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, 4, -2.2f))
                    .fireResistant()));

    public static final DeferredItem<Item> AEGIS_SHIELD = ITEMS.register(
            "aegis_shield",
            () -> new AegisShieldItem(new Item.Properties()
                    .durability(672)
                    .fireResistant()));

    public static final DeferredItem<SwordItem> BACKBITER = ITEMS.register(
            "backbiter",
            () -> new BackbiterItem(ModToolTiers.IMPERIAL_GOLD, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.IMPERIAL_GOLD, 3, -2.3f))
                    .fireResistant()));

    // --- Monster Drops ---
    public static final DeferredItem<Item> MINOTAUR_HORN = ITEMS.registerSimpleItem(
            "minotaur_horn", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> HELLHOUND_FANG = ITEMS.registerSimpleItem(
            "hellhound_fang", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> DRACANAE_SCALE = ITEMS.registerSimpleItem(
            "dracanae_scale", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> CYCLOPS_EYE = ITEMS.registerSimpleItem(
            "cyclops_eye", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> FURY_WHIP_FRAGMENT = ITEMS.registerSimpleItem(
            "fury_whip_fragment", new Item.Properties().stacksTo(16));

    // --- Stygian Iron ---
    public static final DeferredItem<Item> STYGIAN_IRON_INGOT = ITEMS.registerSimpleItem(
            "stygian_iron_ingot", new Item.Properties());
    public static final DeferredItem<SwordItem> STYGIAN_IRON_SWORD = ITEMS.register(
            "stygian_iron_sword",
            () -> new StygianIronSwordItem(ModToolTiers.STYGIAN_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.STYGIAN_IRON, 4, -2.4f))
                    .fireResistant()));

    // --- Spawn Eggs ---
    public static final DeferredItem<Item> MINOTAUR_SPAWN_EGG = ITEMS.register(
            "minotaur_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                    com.demigodsfate.entity.ModEntities.MINOTAUR, 0x5C3317, 0x8B0000,
                    new Item.Properties()));
    public static final DeferredItem<Item> HELLHOUND_SPAWN_EGG = ITEMS.register(
            "hellhound_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                    com.demigodsfate.entity.ModEntities.HELLHOUND, 0x1A1A1A, 0xFF0000,
                    new Item.Properties()));
    public static final DeferredItem<Item> FURY_SPAWN_EGG = ITEMS.register(
            "fury_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                    com.demigodsfate.entity.ModEntities.FURY, 0x2D2D2D, 0xFF6600,
                    new Item.Properties()));

    public static final DeferredItem<Item> MEDUSA_SPAWN_EGG = ITEMS.register(
            "medusa_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                    com.demigodsfate.entity.ModEntities.MEDUSA, 0x2E8B57, 0x808080,
                    new Item.Properties()));

    public static final DeferredItem<Item> EMPOUSAI_SPAWN_EGG = ITEMS.register(
            "empousai_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                    com.demigodsfate.entity.ModEntities.EMPOUSAI, 0xFF69B4, 0x8B0000,
                    new Item.Properties()));
    public static final DeferredItem<Item> CYCLOPS_SPAWN_EGG = ITEMS.register(
            "cyclops_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                    com.demigodsfate.entity.ModEntities.CYCLOPS, 0x8B7355, 0x4A3728,
                    new Item.Properties()));

    // --- Consumables ---
    public static final DeferredItem<Item> AMBROSIA = ITEMS.register(
            "ambrosia", () -> new AmbrosiaItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> NECTAR = ITEMS.register(
            "nectar", () -> new NectarItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> GOLDEN_DRACHMA = ITEMS.registerSimpleItem(
            "golden_drachma", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GREEK_FIRE = ITEMS.register(
            "greek_fire", () -> new GreekFireItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> HERMES_MULTIVITAMIN = ITEMS.register(
            "hermes_multivitamin",
            () -> new com.demigodsfate.item.consumable.HermesMultivitaminItem(new Item.Properties().stacksTo(8)));
}
