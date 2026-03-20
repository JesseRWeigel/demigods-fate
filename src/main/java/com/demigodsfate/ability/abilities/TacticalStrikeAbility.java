package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Next hit is a guaranteed crit with bonus damage. */
public class TacticalStrikeAbility extends Ability {
    public TacticalStrikeAbility() {
        super("tactical_strike", "Tactical Strike", 200, 2, GodParent.ATHENA, GodParent.MINERVA);
    }

    @Override
    public boolean execute(Player player, Level level) {
        // Strength II for 5 seconds — guarantees the next hit is devastating
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 2));
        player.sendSystemMessage(Component.literal("Athena guides your hand — strike now!")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        return true;
    }
}
