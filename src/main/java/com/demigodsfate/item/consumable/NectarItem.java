package com.demigodsfate.item.consumable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Nectar — drink of the gods. Grants Regeneration IV for 30 seconds.
 * Same overconsumption rules as Ambrosia.
 */
public class NectarItem extends Item {
    public NectarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        // Apply Regeneration IV for 30 seconds (600 ticks)
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
        player.sendSystemMessage(Component.literal("The nectar warms you from the inside...")
                .withStyle(ChatFormatting.GOLD));

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        player.getCooldowns().addCooldown(this, 40);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Drink of the Gods").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Regeneration IV for 30 seconds").withStyle(ChatFormatting.GREEN));
    }
}
