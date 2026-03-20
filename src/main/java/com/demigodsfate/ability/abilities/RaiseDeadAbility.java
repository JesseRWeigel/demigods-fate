package com.demigodsfate.ability.abilities;

import com.demigodsfate.ability.Ability;
import com.demigodsfate.godparent.GodParent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Pluto Slot 0 — Summon 3 skeleton warriors that fight for you.
 */
public class RaiseDeadAbility extends Ability {
    private static final int SKELETON_COUNT = 3;

    public RaiseDeadAbility() {
        super("raise_dead", "Raise Dead", 600, 0, GodParent.PLUTO);
    }

    @Override
    public boolean execute(Player player, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.8F, 0.6F);

        // Rising particles
        serverLevel.sendParticles(ParticleTypes.SOUL,
                player.getX(), player.getY(), player.getZ(),
                30, 2.0, 0.5, 2.0, 0.05);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                player.getX(), player.getY(), player.getZ(),
                20, 1.5, 0.2, 1.5, 0.02);

        // Summon 3 skeletons around the player
        for (int i = 0; i < SKELETON_COUNT; i++) {
            double angle = Math.toRadians(i * 120); // Evenly spaced
            double spawnX = player.getX() + Math.cos(angle) * 2.0;
            double spawnZ = player.getZ() + Math.sin(angle) * 2.0;

            Skeleton skeleton = EntityType.SKELETON.create(serverLevel);
            if (skeleton != null) {
                skeleton.moveTo(spawnX, player.getY(), spawnZ, player.getYRot() + i * 120, 0);

                // Equip with iron sword for melee combat
                skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));

                // Make it not burn in sunlight by giving a helmet
                skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));

                // Persist for a limited time (no natural despawn)
                skeleton.setPersistenceRequired();

                serverLevel.addFreshEntity(skeleton);

                // Spawn particles at each skeleton
                serverLevel.sendParticles(ParticleTypes.SMOKE,
                        spawnX, player.getY() + 0.5, spawnZ,
                        15, 0.3, 0.5, 0.3, 0.02);
            }
        }

        return true;
    }
}
