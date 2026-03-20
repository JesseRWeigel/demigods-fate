package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class StormCallAbility extends Ability {
    public StormCallAbility() {
        super("storm_call", "Storm Call", 1200, 2, GodParent.ZEUS, GodParent.JUPITER); // 60s cooldown
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            // Summon a thunderstorm for 6000 ticks (5 minutes)
            serverLevel.setWeatherParameters(0, 6000, true, true);
            return true;
        }
        return false;
    }
}
