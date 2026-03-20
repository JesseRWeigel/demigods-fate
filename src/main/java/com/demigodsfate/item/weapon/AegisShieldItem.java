package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Aegis Shield — Thalia Grace's shield with Medusa's face.
 * Passive: mobs within 5 blocks get Slowness (terror effect).
 * Zeus/Athena affinity: AoE fear pushback on block.
 */
public class AegisShieldItem extends ShieldItem {
    public AegisShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide() || !(entity instanceof Player player)) return;
        if (!isSelected && player.getOffhandItem() != stack) return;

        // Passive: slow nearby mobs (terror from Medusa's face on the shield)
        if (entity.tickCount % 20 == 0) { // Every second
            AABB area = entity.getBoundingBox().inflate(5.0);
            for (Entity nearby : level.getEntities(entity, area)) {
                if (nearby instanceof Mob mob && mob.distanceTo(player) < 5) {
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, true, false));
                }
            }
        }

        // Zeus/Athena affinity: when blocking, push mobs away
        if (player.isBlocking() && entity.tickCount % 40 == 0) {
            GodParent god = GodParentData.getGodParent(player);
            if (god == GodParent.ZEUS || god == GodParent.ATHENA
                    || god == GodParent.JUPITER || god == GodParent.MINERVA) {
                AABB area = entity.getBoundingBox().inflate(4.0);
                for (Entity nearby : level.getEntities(entity, area)) {
                    if (nearby instanceof Mob mob && mob.distanceTo(player) < 4) {
                        Vec3 push = mob.position().subtract(player.position()).normalize().scale(2.0);
                        mob.push(push.x, 0.5, push.z);
                        mob.hurtMarked = true;
                        mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
                    }
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Thalia Grace's Shield")
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Medusa's face radiates terror")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Slows nearby mobs within 5 blocks")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("AoE fear pushback when blocking (Zeus/Athena)")
                .withStyle(ChatFormatting.GOLD));
    }
}
