package com.demigodsfate.entity.monster;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Chimera — lion/goat/snake hybrid. Fire breath + poison tail.
 * Gateway Arch quest boss.
 */
public class ChimeraEntity extends Monster {
    private int fireBreathCooldown = 0;

    public ChimeraEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 35;
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
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ARMOR, 5.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            // Poison tail (30% chance on melee)
            if (random.nextFloat() < 0.3f) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
        }
        return hit;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        if (fireBreathCooldown > 0) fireBreathCooldown--;

        // Fire breath attack when target is in range
        LivingEntity target = getTarget();
        if (target != null && fireBreathCooldown <= 0 && distanceTo(target) < 6) {
            fireBreath();
        }
    }

    private void fireBreath() {
        Vec3 look = getLookAngle();
        AABB area = getBoundingBox().inflate(5.0);

        for (Entity entity : level().getEntities(this, area)) {
            if (entity instanceof LivingEntity target) {
                Vec3 toEntity = target.position().subtract(position()).normalize();
                if (toEntity.dot(look) > 0.4 && target.distanceTo(this) < 6) {
                    target.igniteForSeconds(4);
                    target.hurt(damageSources().mobAttack(this), 5.0f);
                }
            }
        }

        if (level() instanceof ServerLevel sl) {
            for (int i = 0; i < 20; i++) {
                double d = i * 0.3;
                sl.sendParticles(ParticleTypes.FLAME,
                        getX() + look.x * d, getEyeY() + look.y * d, getZ() + look.z * d,
                        2, 0.2, 0.2, 0.2, 0.01);
            }
        }
        fireBreathCooldown = 60; // 3 second cooldown
        playSound(SoundEvents.BLAZE_SHOOT, 1.5f, 0.7f);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 6 + random.nextInt(6)));
        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 8);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.HOGLIN_AMBIENT; }
    @Override
    protected SoundEvent getHurtSound(DamageSource s) { return SoundEvents.HOGLIN_HURT; }
    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.HOGLIN_DEATH; }
}
