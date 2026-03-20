package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Leo's Forge Hammer — sets mobs on fire, auto-smelts mined blocks.
 * Hephaestus affinity: summons fire ring on hit.
 */
public class ForgeHammerItem extends SwordItem {
    public ForgeHammerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (result) {
            // Always sets target on fire
            target.igniteForSeconds(5);

            // Hephaestus affinity: fire ring
            if (attacker instanceof Player player) {
                GodParent god = GodParentData.getGodParent(player);
                if (god == GodParent.HEPHAESTUS) {
                    // Place fire blocks around the target
                    Level level = target.level();
                    BlockPos center = target.blockPosition();
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dz == 0) continue;
                            BlockPos firePos = center.offset(dx, 0, dz);
                            if (level.isEmptyBlock(firePos) && level.getBlockState(firePos.below()).isSolid()) {
                                level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Leo Valdez's Hammer")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Sets enemies on fire")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Fire ring on hit (Hephaestus)")
                .withStyle(ChatFormatting.GOLD));
    }
}
