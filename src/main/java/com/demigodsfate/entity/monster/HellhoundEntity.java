package com.demigodsfate.entity.monster;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
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
 * Hellhound — fast shadow-teleporting pack hunters.
 * Uses Wolf model/size but with monster AI. Spawns at night in forests.
 */
public class HellhoundEntity extends Monster {
    private int teleportCooldown = 0;

    public HellhoundEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 15;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.5f));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, HellhoundEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.MOVEMENT_SPEED, 0.38) // Very fast
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        if (teleportCooldown > 0) teleportCooldown--;

        // Shadow teleport: when target is far but within tracking range
        LivingEntity target = getTarget();
        if (target != null && teleportCooldown <= 0) {
            double dist = distanceTo(target);
            if (dist > 10 && dist < 24 && random.nextFloat() < 0.02f) { // ~1% per tick chance
                shadowTeleport(target);
            }
        }

        // Shadow particles
        if (level() instanceof ServerLevel sl && tickCount % 10 == 0) {
            sl.sendParticles(ParticleTypes.SMOKE,
                    getX(), getY() + 0.3, getZ(), 2, 0.2, 0.1, 0.2, 0.01);
        }
    }

    private void shadowTeleport(LivingEntity target) {
        double angle = random.nextDouble() * Math.PI * 2;
        double dist = 3.0 + random.nextDouble() * 2;
        double tx = target.getX() + Math.cos(angle) * dist;
        double tz = target.getZ() + Math.sin(angle) * dist;

        if (level() instanceof ServerLevel sl) {
            // Departure particles
            sl.sendParticles(ParticleTypes.LARGE_SMOKE,
                    getX(), getY() + 0.5, getZ(), 10, 0.3, 0.5, 0.3, 0.05);
        }

        this.teleportTo(tx, target.getY(), tz);
        teleportCooldown = 100; // 5 second cooldown

        if (level() instanceof ServerLevel sl) {
            // Arrival particles
            sl.sendParticles(ParticleTypes.LARGE_SMOKE,
                    getX(), getY() + 0.5, getZ(), 10, 0.3, 0.5, 0.3, 0.05);
        }

        playSound(SoundEvents.ENDERMAN_TELEPORT, 0.5f, 0.5f);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);
        // Hellhound Fang
        if (random.nextFloat() < 0.4f) {
            this.spawnAtLocation(new ItemStack(ModItems.HELLHOUND_FANG.get(), 1));
        }
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 2 + random.nextInt(3)));

        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 3);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.WOLF_GROWL; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WOLF_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.WOLF_DEATH; }
}
