package com.demigodsfate.event;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Drachma shop — spend drachmas to buy divine weapons and items.
 * /shop list — show items for sale
 * /shop buy <number> — purchase an item
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class DrachmaShopHandler {

    private record ShopItem(String name, int cost, java.util.function.Supplier<ItemStack> item) {}

    private static final ShopItem[] SHOP_ITEMS = {
            new ShopItem("Riptide", 30, () -> new ItemStack(ModItems.RIPTIDE.get())),
            new ShopItem("Ivlivs", 30, () -> new ItemStack(ModItems.IVLIVS.get())),
            new ShopItem("Aegis Shield", 35, () -> new ItemStack(ModItems.AEGIS_SHIELD.get())),
            new ShopItem("Backbiter", 25, () -> new ItemStack(ModItems.BACKBITER.get())),
            new ShopItem("Forge Hammer", 30, () -> new ItemStack(ModItems.FORGE_HAMMER.get())),
            new ShopItem("Katoptris", 25, () -> new ItemStack(ModItems.KATOPTRIS.get())),
            new ShopItem("Morphing Spear", 30, () -> new ItemStack(ModItems.MORPHING_SPEAR.get())),
            new ShopItem("Stygian Iron Sword", 50, () -> new ItemStack(ModItems.STYGIAN_IRON_SWORD.get())),
            new ShopItem("Ambrosia x5", 10, () -> new ItemStack(ModItems.AMBROSIA.get(), 5)),
            new ShopItem("Nectar x5", 10, () -> new ItemStack(ModItems.NECTAR.get(), 5)),
            new ShopItem("Greek Fire x3", 15, () -> new ItemStack(ModItems.GREEK_FIRE.get(), 3)),
            new ShopItem("Hermes Multivitamins x3", 8, () -> new ItemStack(ModItems.HERMES_MULTIVITAMIN.get(), 3)),
            new ShopItem("Celestial Bronze Ingot x5", 5, () -> new ItemStack(ModItems.CELESTIAL_BRONZE_INGOT.get(), 5)),
            new ShopItem("Imperial Gold Ingot x5", 8, () -> new ItemStack(ModItems.IMPERIAL_GOLD_INGOT.get(), 5)),
            new ShopItem("Stygian Iron Ingot x2", 20, () -> new ItemStack(ModItems.STYGIAN_IRON_INGOT.get(), 2)),
    };

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("shop")
                .then(Commands.literal("list")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof Player player)) return 0;

                            int drachmas = GodParentData.getDrachmas(player);
                            player.sendSystemMessage(Component.literal("=== Camp Store ===")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                            player.sendSystemMessage(Component.literal("Your drachmas: " + drachmas)
                                    .withStyle(ChatFormatting.GOLD));
                            player.sendSystemMessage(Component.literal(""));

                            for (int i = 0; i < SHOP_ITEMS.length; i++) {
                                ShopItem item = SHOP_ITEMS[i];
                                ChatFormatting color = drachmas >= item.cost ? ChatFormatting.GREEN : ChatFormatting.RED;
                                player.sendSystemMessage(Component.literal(
                                        "  [" + (i + 1) + "] " + item.name + " — " + item.cost + " drachmas")
                                        .withStyle(color));
                            }
                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal("Use /shop buy <number> to purchase")
                                    .withStyle(ChatFormatting.GRAY));
                            return 1;
                        }))
                .then(Commands.literal("buy")
                        .then(Commands.argument("number", IntegerArgumentType.integer(1, SHOP_ITEMS.length))
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof Player player)) return 0;

                                    int num = IntegerArgumentType.getInteger(context, "number") - 1;
                                    ShopItem item = SHOP_ITEMS[num];

                                    if (!GodParentData.spendDrachmas(player, item.cost)) {
                                        player.sendSystemMessage(Component.literal("Not enough drachmas! Need " + item.cost + ", have " + GodParentData.getDrachmas(player))
                                                .withStyle(ChatFormatting.RED));
                                        return 0;
                                    }

                                    player.getInventory().add(item.item.get());
                                    player.sendSystemMessage(Component.literal("Purchased: " + item.name + "! (-" + item.cost + " drachmas)")
                                            .withStyle(ChatFormatting.GREEN));
                                    return 1;
                                }))));
    }
}
