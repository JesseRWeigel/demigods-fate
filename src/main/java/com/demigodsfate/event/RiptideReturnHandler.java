package com.demigodsfate.event;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Riptide always returns to the player's pocket.
 * - Can't be dropped (Q key blocked)
 * - Returns on death
 * - If somehow lost, reappears in inventory every 5 seconds
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class RiptideReturnHandler {

    // Track which players have ever held Riptide
    private static final Set<UUID> riptideOwners = new HashSet<>();

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if (stack.is(ModItems.RIPTIDE.get())) {
            event.setCanceled(true);
            Player player = event.getPlayer();
            player.sendSystemMessage(Component.literal("Riptide returns to your pocket...")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        // Track Riptide ownership
        boolean hasRiptide = false;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (player.getInventory().getItem(i).is(ModItems.RIPTIDE.get())) {
                hasRiptide = true;
                riptideOwners.add(player.getUUID());
                break;
            }
        }

        // If player owned Riptide but lost it, give it back every 5 seconds
        if (!hasRiptide && riptideOwners.contains(player.getUUID()) && player.tickCount % 100 == 0) {
            player.getInventory().add(new ItemStack(ModItems.RIPTIDE.get()));
            player.sendSystemMessage(Component.literal("Riptide reappears in your pocket!")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();

        // Check if they had Riptide or were a known owner
        boolean hadRiptide = riptideOwners.contains(original.getUUID());
        if (!hadRiptide) {
            for (int i = 0; i < original.getInventory().getContainerSize(); i++) {
                if (original.getInventory().getItem(i).is(ModItems.RIPTIDE.get())) {
                    hadRiptide = true;
                    break;
                }
            }
        }

        if (hadRiptide) {
            newPlayer.getInventory().add(new ItemStack(ModItems.RIPTIDE.get()));
            riptideOwners.add(newPlayer.getUUID());
        }
    }
}
