package com.demigodsfate.event;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Riptide always returns to the player's pocket.
 * When dropped, it reappears in the player's inventory after 3 seconds.
 * When the player dies, it stays in their inventory on respawn.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class RiptideReturnHandler {

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if (stack.is(ModItems.RIPTIDE.get())) {
            // Cancel the drop — Riptide can't be thrown away
            event.setCanceled(true);

            Player player = event.getPlayer();
            player.sendSystemMessage(Component.literal("Riptide returns to your pocket...")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        // If the original player had Riptide, give it to the new player
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();

        for (int i = 0; i < original.getInventory().getContainerSize(); i++) {
            ItemStack stack = original.getInventory().getItem(i);
            if (stack.is(ModItems.RIPTIDE.get())) {
                newPlayer.getInventory().add(stack.copy());
                break;
            }
        }
    }
}
