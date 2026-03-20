package com.demigodsfate.item.weapon;

import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Frank Zhang's Morphing Spear — right-click cycles weapon forms.
 * Mars affinity: +20% damage in every form.
 */
public class MorphingSpearItem extends SwordItem {
    private static final String[] FORM_NAMES = {"Spear", "Sword", "War Axe"};
    private static final Map<UUID, Integer> playerForms = new HashMap<>();

    public MorphingSpearItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            int currentForm = playerForms.getOrDefault(player.getUUID(), 0);
            int nextForm = (currentForm + 1) % 3;
            playerForms.put(player.getUUID(), nextForm);

            player.sendSystemMessage(Component.literal("Form: " + FORM_NAMES[nextForm])
                    .withStyle(ChatFormatting.GOLD));
            player.getCooldowns().addCooldown(this, 10);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (result && attacker instanceof Player player) {
            GodParent god = GodParentData.getGodParent(player);
            if (god == GodParent.MARS) {
                target.hurt(player.damageSources().playerAttack(player), 2.0f);
            }
        }
        return result;
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Frank Zhang's Gift from Mars")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Right-click to change form")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("+20% damage in all forms (Mars)")
                .withStyle(ChatFormatting.RED));
    }
}
