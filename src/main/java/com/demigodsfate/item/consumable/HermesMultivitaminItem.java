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

import java.util.List;

/**
 * Hermes Multivitamins — clears all negative effects and grants 10 seconds potion immunity.
 */
public class HermesMultivitaminItem extends Item {
    public HermesMultivitaminItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            // Remove all negative effects
            player.getActiveEffects().stream()
                    .filter(e -> !e.getEffect().value().isBeneficial())
                    .map(e -> e.getEffect())
                    .toList()
                    .forEach(player::removeEffect);

            player.sendSystemMessage(Component.literal("All negative effects cleared!")
                    .withStyle(ChatFormatting.GREEN));

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            player.getCooldowns().addCooldown(this, 200); // 10 second cooldown
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Hermes's Multivitamins").withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Clears all negative effects").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.literal("10s potion immunity").withStyle(ChatFormatting.AQUA));
    }
}
