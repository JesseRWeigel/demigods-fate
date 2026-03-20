package com.demigodsfate.entity.monster;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.phys.Vec3;

/**
 * The Minotaur — iconic Percy Jackson intro boss.
 * Behavior: Charges in straight lines, devastating melee.
 * Uses a Ravager model placeholder (large, aggressive biped).
 */
public class MinotaurEntity extends Monster {
    private static final EntityDataAccessor<Boolean> CHARGING =
            SynchedEntityData.defineId(MinotaurEntity.class, EntityDataSerializers.BOOLEAN);

    private int chargeTimer = 0;
    private int chargeCooldown = 0;
    private Vec3 chargeTarget = null;

    public MinotaurEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 50;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0) // 40 hearts — boss-tier
                .add(Attributes.ATTACK_DAMAGE, 12.0) // 6 hearts per hit
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) return;

        // Charge mechanic
        if (chargeCooldown > 0) chargeCooldown--;

        LivingEntity target = getTarget();
        if (target != null && chargeCooldown <= 0 && !isCharging()) {
            double dist = distanceTo(target);
            // Start charge when 8-20 blocks away
            if (dist > 8 && dist < 20) {
                startCharge(target);
            }
        }

        if (isCharging()) {
            chargeTimer--;
            if (chargeTimer <= 0 || chargeTarget == null) {
                stopCharge();
                return;
            }

            // Rush forward in the charge direction
            Vec3 dir = chargeTarget.subtract(position()).normalize();
            setDeltaMovement(dir.x * 1.5, getDeltaMovement().y, dir.z * 1.5);
            hurtMarked = true;

            // Damage anything in the charge path
            for (Entity entity : level().getEntities(this, getBoundingBox().inflate(0.5))) {
                if (entity instanceof LivingEntity living && !(entity instanceof MinotaurEntity)) {
                    living.hurt(damageSources().mobAttack(this), 15.0f); // Devastating charge
                    living.push(dir.x * 2, 0.5, dir.z * 2);
                    living.hurtMarked = true;
                }
            }

            // Particles during charge
            if (level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.CLOUD,
                        getX(), getY() + 0.5, getZ(),
                        3, 0.3, 0.1, 0.3, 0.05);
            }
        }
    }

    private void startCharge(LivingEntity target) {
        entityData.set(CHARGING, true);
        chargeTimer = 30; // 1.5 seconds of charge
        chargeTarget = target.position();
        chargeCooldown = 100; // 5 second cooldown between charges
        playSound(SoundEvents.RAVAGER_ROAR, 2.0f, 0.6f);
    }

    private void stopCharge() {
        entityData.set(CHARGING, false);
        chargeTimer = 0;
        chargeTarget = null;
    }

    public boolean isCharging() {
        return entityData.get(CHARGING);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);

        // Drop Minotaur Horn
        this.spawnAtLocation(new ItemStack(ModItems.MINOTAUR_HORN.get(), 1 + random.nextInt(2)));

        // Drop drachmas
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 5 + random.nextInt(6)));

        // 20% chance to drop ambrosia
        if (random.nextFloat() < 0.2f) {
            this.spawnAtLocation(new ItemStack(ModItems.AMBROSIA.get(), 1));
        }

        // Give drachmas directly to the killer
        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 10);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.RAVAGER_AMBIENT; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.RAVAGER_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.RAVAGER_DEATH; }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ChargeCooldown", chargeCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        chargeCooldown = tag.getInt("ChargeCooldown");
    }
}
