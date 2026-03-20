package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Ivlivs — Jason Grace's Imperial Gold coin that transforms into a sword or lance.
 * Jupiter affinity: lightning damage on hit.
 */
public class IvlivsItem extends SwordItem {
    public IvlivsItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        // Jupiter/Zeus affinity: lightning on hit
        if (result && attacker instanceof Player player) {
            GodParent god = GodParentData.getGodParent(player);
            if ((god == GodParent.JUPITER || god == GodParent.ZEUS)
                    && player.level() instanceof ServerLevel serverLevel
                    && player.getRandom().nextFloat() < 0.25f) { // 25% chance
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (bolt != null) {
                    bolt.moveTo(target.getX(), target.getY(), target.getZ());
                    bolt.setCause(player instanceof net.minecraft.server.level.ServerPlayer sp ? sp : null);
                    serverLevel.addFreshEntity(bolt);
                }
            }
        }
        return result;
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Jason Grace's Imperial Gold Coin")
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Flip to transform between sword and lance")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("25% lightning strike on hit (Jupiter/Zeus)")
                .withStyle(ChatFormatting.GOLD));
    }
}
