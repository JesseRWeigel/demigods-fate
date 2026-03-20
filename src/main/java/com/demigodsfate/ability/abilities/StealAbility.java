package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Gives Luck effect for better loot + invisibility for quick steal. */
public class StealAbility extends Ability {
    public StealAbility() {
        super("steal", "Steal", 150, 1, GodParent.HERMES);
    }

    @Override
    public boolean execute(Player player, Level level) {
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 300, 2));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0));
        player.sendSystemMessage(Component.literal("Hermes guides your fingers — quick, grab what you can!")
                .withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
        return true;
    }
}
