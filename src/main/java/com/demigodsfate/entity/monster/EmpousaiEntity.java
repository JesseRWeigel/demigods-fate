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

/**
 * Empousai — disguise as beautiful women (use villager appearance).
 * Charmspeak stun on first attack, then drain blood (fire damage).
 * Teleport away when low health.
 */
public class EmpousaiEntity extends Monster {
    private boolean hasCharmed = false;

    public EmpousaiEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 18;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 28.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.FOLLOW_RANGE, 20.0);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // First attack: charmspeak stun
        if (!hasCharmed && target instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0));
            hasCharmed = true;

            if (level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.HEART,
                        player.getX(), player.getEyeY(), player.getZ(),
                        5, 0.5, 0.3, 0.5, 0.0);
            }
        }
        return super.doHurtTarget(target);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        // Teleport away when low health (fire teleport)
        if (getHealth() < getMaxHealth() * 0.25f && random.nextFloat() < 0.05f) {
            double tx = getX() + (random.nextDouble() - 0.5) * 16;
            double tz = getZ() + (random.nextDouble() - 0.5) * 16;
            if (level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.FLAME,
                        getX(), getY() + 0.5, getZ(), 15, 0.3, 0.5, 0.3, 0.1);
            }
            teleportTo(tx, getY(), tz);
            playSound(SoundEvents.ENDERMAN_TELEPORT, 0.8f, 1.2f);
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 3 + random.nextInt(3)));
        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 4);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.WITCH_AMBIENT; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WITCH_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.WITCH_DEATH; }
}
