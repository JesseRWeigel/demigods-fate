package com.demigodsfate.entity.monster;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Fury (Kindly One) — flying whip-wielding servants of Hades.
 * Hard difficulty, triggered by quest events. Pursues relentlessly.
 * Uses Vex-like flying behavior.
 */
public class FuryEntity extends Monster {
    public FuryEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 25;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 16.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0) // Hard mob
                .add(Attributes.ATTACK_DAMAGE, 9.0) // Fire whip damage
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.FOLLOW_RANGE, 48.0); // Pursues relentlessly
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            // Fire whip: set target on fire
            living.igniteForSeconds(3);
        }
        return hit;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 4 + random.nextInt(5)));
        // Fury Whip Fragment
        if (random.nextFloat() < 0.35f) {
            this.spawnAtLocation(new ItemStack(ModItems.FURY_WHIP_FRAGMENT.get(), 1));
        }
        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 5);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.VEX_AMBIENT; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.VEX_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.VEX_DEATH; }
}
