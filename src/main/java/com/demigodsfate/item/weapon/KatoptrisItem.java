package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Katoptris — Helen of Troy's dagger, now Piper McLean's.
 * Shows nearby enemies through walls (Glowing effect).
 * Aphrodite/Venus affinity: charmed mobs don't attack.
 */
public class KatoptrisItem extends SwordItem {
    public KatoptrisItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (level.isClientSide() || !(entity instanceof Player player)) return;
        if (!selected && player.getOffhandItem() != stack) return;

        // Reveal nearby enemies every 2 seconds
        if (entity.tickCount % 40 == 0) {
            for (Entity nearby : level.getEntities(entity, entity.getBoundingBox().inflate(15))) {
                if (nearby instanceof LivingEntity living && living instanceof net.minecraft.world.entity.monster.Monster) {
                    living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, true, false));
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Helen of Troy's Dagger")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Reveals nearby enemies through walls")
                .withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.literal("Charmed mobs don't attack (Aphrodite/Venus)")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
