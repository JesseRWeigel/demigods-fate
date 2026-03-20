package com.demigodsfate.item.consumable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Ambrosia — food of the gods. Heals 8 hearts instantly.
 * Max 3 per in-game day. More than 3 deals fire damage (divine overdose).
 */
public class AmbrosiaItem extends Item {
    private static final int MAX_PER_DAY = 3;
    private static final float HEAL_AMOUNT = 16.0f; // 8 hearts
    private static final Map<UUID, DailyTracker> usageTracker = new HashMap<>();

    public AmbrosiaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        DailyTracker tracker = usageTracker.computeIfAbsent(player.getUUID(), k -> new DailyTracker());
        long currentDay = level.getDayTime() / 24000;
        tracker.resetIfNewDay(currentDay);

        if (tracker.count >= MAX_PER_DAY) {
            // Divine overdose — too much ambrosia burns you
            player.hurt(player.damageSources().onFire(), 6.0f);
            player.sendSystemMessage(Component.literal("The ambrosia burns through you — too much divine food!")
                    .withStyle(ChatFormatting.RED));
        } else {
            // Heal 8 hearts
            player.heal(HEAL_AMOUNT);
            player.sendSystemMessage(Component.literal("The ambrosia tastes like your favorite comfort food...")
                    .withStyle(ChatFormatting.GOLD));
        }

        tracker.count++;
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        // Cooldown to prevent spam
        player.getCooldowns().addCooldown(this, 40); // 2 seconds

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Food of the Gods").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Heals 8 hearts instantly").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("Max 3/day — more causes burning!").withStyle(ChatFormatting.RED));
    }

    private static class DailyTracker {
        long lastDay = -1;
        int count = 0;

        void resetIfNewDay(long currentDay) {
            if (currentDay != lastDay) {
                lastDay = currentDay;
                count = 0;
            }
        }
    }
}
