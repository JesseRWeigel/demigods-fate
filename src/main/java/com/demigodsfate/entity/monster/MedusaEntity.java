package com.demigodsfate.entity.monster;

import com.demigodsfate.effect.ModEffects;
import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Medusa — boss entity with stone gaze mechanic.
 * Players looking directly at her get the Stone Gaze effect (extreme slowness).
 * Must fight without looking at her, or use a reflective shield.
 */
public class MedusaEntity extends Monster {
    private static final double GAZE_RANGE = 12.0;
    private static final double GAZE_ANGLE_THRESHOLD = 0.7; // Cosine of ~45 degrees

    public MedusaEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 40;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0) // 30 hearts - boss
                .add(Attributes.ATTACK_DAMAGE, 6.0) // Snake hair melee
                .add(Attributes.MOVEMENT_SPEED, 0.25) // Slow — her gaze is the danger
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        // Stone gaze mechanic: check if any nearby players are looking at Medusa
        if (tickCount % 10 == 0) { // Check every 0.5 seconds
            for (Player player : level().getEntitiesOfClass(Player.class,
                    getBoundingBox().inflate(GAZE_RANGE))) {
                if (isPlayerLookingAtMe(player)) {
                    applyStoneGaze(player);
                }
            }
        }
    }

    /**
     * Check if a player is looking directly at Medusa.
     * Uses dot product of player look vector and direction to Medusa.
     */
    private boolean isPlayerLookingAtMe(Player player) {
        Vec3 playerLook = player.getLookAngle().normalize();
        Vec3 toMedusa = this.getEyePosition().subtract(player.getEyePosition()).normalize();

        double dot = playerLook.dot(toMedusa);
        return dot > GAZE_ANGLE_THRESHOLD; // Player is facing Medusa
    }

    /**
     * Apply stone gaze — increasingly severe the longer they look.
     */
    private void applyStoneGaze(Player player) {
        // Check if player is using a shield (reflective surface — blocks the gaze!)
        if (player.isBlocking()) {
            // The gaze reflects back! Damage Medusa instead
            this.hurt(player.damageSources().magic(), 5.0f);

            if (level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.FLASH,
                        getX(), getEyeY(), getZ(), 1, 0, 0, 0, 0);
            }
            return;
        }

        // Apply Stone Gaze effect (extreme slowness + damage at high stacks)
        MobEffectInstance current = player.getEffect(ModEffects.STONE_GAZE);
        int currentAmplifier = current != null ? current.getAmplifier() : -1;
        int newAmplifier = Math.min(currentAmplifier + 1, 4); // Max amplifier 4

        player.addEffect(new MobEffectInstance(ModEffects.STONE_GAZE, 60, newAmplifier));

        // Visual feedback
        if (level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.ASH,
                    player.getX(), player.getEyeY(), player.getZ(),
                    5, 0.3, 0.3, 0.3, 0.01);
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);

        // Medusa's head — a trophy (player head as placeholder)
        if (random.nextFloat() < 0.5f) {
            this.spawnAtLocation(new ItemStack(Items.PLAYER_HEAD, 1));
        }

        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 8 + random.nextInt(8)));

        // Guaranteed ambrosia from boss
        this.spawnAtLocation(new ItemStack(ModItems.AMBROSIA.get(), 1));

        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 15);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.CAT_HISS; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WITCH_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.WITCH_DEATH; }
}
