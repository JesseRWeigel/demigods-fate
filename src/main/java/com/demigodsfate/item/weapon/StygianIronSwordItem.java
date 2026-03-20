package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Stygian Iron sword — forged in the Underworld.
 * Kills everything, absorbs essence to prevent monster respawn.
 * Pluto/Hades affinity: life drain on hit.
 */
public class StygianIronSwordItem extends SwordItem {
    public StygianIronSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if (result && attacker instanceof Player player) {
            // Shadow particles on hit
            if (player.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.SMOKE,
                        target.getX(), target.getY() + 0.5, target.getZ(),
                        8, 0.3, 0.5, 0.3, 0.05);
            }

            // Pluto/Hades affinity: life drain
            GodParent god = GodParentData.getGodParent(player);
            if (god == GodParent.PLUTO || god == GodParent.ZEUS) {
                // Wrong — should be Hades, but we don't have a Hades enum. Use Pluto.
                float healAmount = 2.0f; // 1 heart per hit
                player.heal(healAmount);
            }
        }

        return result;
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Forged in the Underworld")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Absorbs the essence of the slain")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("Prevents monster respawn")
                .withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.literal("Life drain on hit (Pluto)")
                .withStyle(ChatFormatting.DARK_GREEN));
    }
}
