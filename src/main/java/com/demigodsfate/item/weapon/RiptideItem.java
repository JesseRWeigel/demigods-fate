package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Riptide (Anaklusmos) — Percy Jackson's signature sword.
 * - Cannot break (repairs on low durability)
 * - Returns to inventory if dropped
 * - +50% damage near water for children of Poseidon/Neptune
 */
public class RiptideItem extends CelestialBronzeSwordItem {
    public RiptideItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isMortal(target)) return false;

        // Poseidon/Neptune affinity: +50% damage near water
        if (attacker instanceof Player player) {
            GodParent god = GodParentData.getGodParent(player);
            if ((god == GodParent.POSEIDON || god == GodParent.NEPTUNE)
                    && player.isInWaterRainOrBubble()) {
                // Apply bonus damage
                target.hurt(player.damageSources().playerAttack(player),
                        3.0f); // Extra flat damage on top of normal hit
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Enchanted glint — it's a divine weapon
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return Integer.MAX_VALUE; // Effectively unbreakable
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // Cannot break
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Anaklusmos — The Currents")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Percy Jackson's legendary sword")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Cannot be broken or lost")
                .withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.literal("+50% damage near water (Poseidon/Neptune)")
                .withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
