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
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Cyclops — huge, slow, massive damage. Throws boulders (fireballs as placeholder).
 * Found in caves and the Sea of Monsters.
 */
public class CyclopsEntity extends Monster {
    private int throwCooldown = 0;

    public CyclopsEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 30;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 20.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0) // 30 hearts
                .add(Attributes.ATTACK_DAMAGE, 14.0) // 7 hearts per punch
                .add(Attributes.MOVEMENT_SPEED, 0.22) // Slow
                .add(Attributes.ARMOR, 8.0) // Tanky
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        if (throwCooldown > 0) throwCooldown--;

        // Throw boulder (fireball) when target is far
        LivingEntity target = getTarget();
        if (target != null && throwCooldown <= 0) {
            double dist = distanceTo(target);
            if (dist > 6 && dist < 20) {
                throwBoulder(target);
            }
        }
    }

    private void throwBoulder(LivingEntity target) {
        Vec3 dir = target.position().subtract(position()).normalize();
        LargeFireball fireball = new LargeFireball(level(), this, dir, 1);
        fireball.setPos(getX(), getEyeY(), getZ());
        level().addFreshEntity(fireball);
        throwCooldown = 80; // 4 second cooldown
        playSound(SoundEvents.IRON_GOLEM_ATTACK, 2.0f, 0.5f);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);
        // Drop iron (cyclopes are smiths) and cyclops eye
        this.spawnAtLocation(new ItemStack(net.minecraft.world.item.Items.IRON_INGOT, 3 + random.nextInt(5)));
        if (random.nextFloat() < 0.3f) {
            this.spawnAtLocation(new ItemStack(ModItems.CYCLOPS_EYE.get(), 1));
        }
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 5 + random.nextInt(5)));
        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 7);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.IRON_GOLEM_HURT; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.IRON_GOLEM_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.IRON_GOLEM_DEATH; }
}
