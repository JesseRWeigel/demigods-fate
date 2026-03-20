package com.demigodsfate.event;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.minecraft.world.entity.item.ItemEntity;

/**
 * Adds drachma drops to vanilla hostile mobs and bonus drops for
 * mobs killed with celestial bronze / imperial gold weapons.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class MonsterDropHandler {

    @SubscribeEvent
    public static void onMobDrop(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        // Only add drops if killed by a player
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        var entity = event.getEntity();

        // Add golden drachma drops to hostile mobs
        if (entity instanceof Monster) {
            int drachmaCount = 0;

            // Scale drachmas by mob difficulty
            if (entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Spider) {
                drachmaCount = 1 + entity.level().random.nextInt(2); // 1-2
            } else if (entity instanceof Creeper || entity instanceof Witch || entity instanceof Pillager) {
                drachmaCount = 2 + entity.level().random.nextInt(3); // 2-4
            } else if (entity instanceof Blaze || entity instanceof WitherSkeleton || entity instanceof Vindicator) {
                drachmaCount = 3 + entity.level().random.nextInt(4); // 3-6
            } else {
                drachmaCount = 1; // Default for other hostiles
            }

            // Add drachmas directly to player balance (cleaner than item drops)
            GodParentData.addDrachmas(player, drachmaCount);

            // Also drop 1-2 physical drachma items for visual feedback
            if (entity.level().random.nextFloat() < 0.3f) { // 30% chance
                ItemStack drachmaStack = new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 1);
                ItemEntity itemEntity = new ItemEntity(entity.level(),
                        entity.getX(), entity.getY() + 0.5, entity.getZ(), drachmaStack);
                event.getDrops().add(itemEntity);
            }
        }
    }
}
