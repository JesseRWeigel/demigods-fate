package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Summon a temporary combat ally (iron golem as placeholder for bronze automaton). */
public class BronzeAutomatonAbility extends Ability {
    public BronzeAutomatonAbility() {
        super("bronze_automaton", "Bronze Automaton", 1200, 2, GodParent.HEPHAESTUS); // 60s cooldown
    }

    @Override
    public boolean execute(Player player, Level level) {
        IronGolem golem = EntityType.IRON_GOLEM.create(level);
        if (golem != null) {
            golem.moveTo(player.getX() + 2, player.getY(), player.getZ());
            golem.setPlayerCreated(true);
            // Make it temporary — despawn after 30 seconds
            golem.setCustomName(net.minecraft.network.chat.Component.literal("Bronze Automaton"));
            golem.setCustomNameVisible(true);
            level.addFreshEntity(golem);
            return true;
        }
        return false;
    }
}
