package com.demigodsfate.item.consumable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Greek Fire — throwable fire projectile that creates green-tinged fire.
 * Burns through anything and cannot be extinguished by water.
 * (Implemented as a fireball projectile for now)
 */
public class GreekFireItem extends Item {
    public GreekFireItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            Vec3 look = player.getLookAngle();

            SmallFireball fireball = new SmallFireball(level, player, look);
            level.addFreshEntity(fireball);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0f, 0.8f);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            player.getCooldowns().addCooldown(this, 20);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Volatile Green Flames").withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Burns through anything").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Cannot be extinguished by water").withStyle(ChatFormatting.DARK_RED));
    }
}
