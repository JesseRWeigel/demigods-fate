package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/** Place a trap at the target location (TNT-based for now). */
public class MechanicalTrapAbility extends Ability {
    public MechanicalTrapAbility() {
        super("mechanical_trap", "Mechanical Trap", 300, 1, GodParent.HEPHAESTUS);
    }

    @Override
    public boolean execute(Player player, Level level) {
        HitResult hit = player.pick(10.0, 0.0f, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hit).getBlockPos().above();
            if (level.isEmptyBlock(pos)) {
                // Place a pressure plate + TNT trap
                level.setBlock(pos.below(), Blocks.TNT.defaultBlockState(), 3);
                level.setBlock(pos, Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 3);
                return true;
            }
        }
        return false;
    }
}
