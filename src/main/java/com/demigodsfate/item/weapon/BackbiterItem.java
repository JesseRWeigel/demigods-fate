package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Backbiter — Luke Castellan's hybrid sword.
 * Half celestial bronze, half mortal steel: damages everything.
 * Hermes affinity: steal an item on kill.
 */
public class BackbiterItem extends SwordItem {
    public BackbiterItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        // Hermes affinity: steal loot on kill
        if (result && target.isDeadOrDying() && attacker instanceof Player player) {
            GodParent god = GodParentData.getGodParent(player);
            if (god == GodParent.HERMES) {
                // Drop extra random loot
                ItemStack bonus = new ItemStack(net.minecraft.world.item.Items.GOLD_INGOT,
                        1 + player.getRandom().nextInt(3));
                ItemEntity drop = new ItemEntity(player.level(),
                        target.getX(), target.getY() + 0.5, target.getZ(), bonus);
                player.level().addFreshEntity(drop);
            }
        }
        return result;
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Luke Castellan's Cursed Blade")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Half celestial bronze, half mortal steel")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Damages both monsters and mortals")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Steal gold on kill (Hermes)")
                .withStyle(ChatFormatting.GREEN));
    }
}
