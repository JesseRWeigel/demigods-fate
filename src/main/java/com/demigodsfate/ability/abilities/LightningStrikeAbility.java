package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class LightningStrikeAbility extends Ability {
    public LightningStrikeAbility() {
        super("lightning_strike", "Lightning Strike", 150, 0, GodParent.ZEUS, GodParent.JUPITER);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Raycast to where player is looking (up to 30 blocks)
        HitResult hit = player.pick(30.0, 0.0f, false);
        BlockPos target;
        if (hit.getType() == HitResult.Type.BLOCK) {
            target = ((BlockHitResult) hit).getBlockPos();
        } else {
            target = player.blockPosition().offset(
                    (int)(player.getLookAngle().x * 15),
                    0,
                    (int)(player.getLookAngle().z * 15));
        }

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
        if (bolt != null) {
            bolt.moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
            bolt.setCause(player instanceof net.minecraft.server.level.ServerPlayer sp ? sp : null);
            serverLevel.addFreshEntity(bolt);
        }
        return true;
    }
}
