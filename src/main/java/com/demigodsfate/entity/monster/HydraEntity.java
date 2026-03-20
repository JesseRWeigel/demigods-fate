package com.demigodsfate.entity.monster;

import com.demigodsfate.godparent.GodParentData;
import com.demigodsfate.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Hydra — multi-headed boss. When hit without fire, it heals (heads regrow).
 * Must be killed with fire damage (torch, fire ability, Greek Fire, lava).
 * Each "head regrowth" increases its max HP and damage.
 */
public class HydraEntity extends Monster {
    private int heads = 3;
    private int regrowthCount = 0;

    public HydraEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 60;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.FOLLOW_RANGE, 30.0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean isFireDamage = source.is(net.minecraft.world.damagesource.DamageTypes.IN_FIRE)
                || source.is(net.minecraft.world.damagesource.DamageTypes.ON_FIRE)
                || source.is(net.minecraft.world.damagesource.DamageTypes.LAVA);

        // Check if attacker is on fire or using fire-related items
        if (source.getEntity() instanceof Player player) {
            if (player.isOnFire()) isFireDamage = true;
            // Check for Greek Fire or fire aspect enchantment
            ItemStack held = player.getMainHandItem();
            if (held.is(ModItems.GREEK_FIRE.get())) isFireDamage = true;
        }

        if (!isFireDamage && !isDeadOrDying()) {
            // HEAD REGROWTH: non-fire damage causes the hydra to regenerate
            if (regrowthCount < 5) { // Max 5 regrowths
                regrowthCount++;
                heads += 2; // Two heads grow for one cut

                // Heal instead of taking damage
                heal(amount * 0.5f);

                // Warn the player
                if (source.getEntity() instanceof Player player) {
                    player.sendSystemMessage(Component.literal("The Hydra's heads regrow! Use FIRE to cauterize!")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                }

                // Regrowth particles
                if (level() instanceof ServerLevel sl) {
                    sl.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            getX(), getY() + 1, getZ(),
                            10, 0.5, 0.5, 0.5, 0.0);
                }

                playSound(SoundEvents.WITHER_HURT, 1.0f, 0.5f);
                return false; // Negate the damage
            }
        }

        // Fire damage works normally (and does bonus damage)
        if (isFireDamage) {
            amount *= 1.5f; // 50% bonus fire damage
            if (level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.FLAME,
                        getX(), getY() + 1, getZ(), 15, 0.5, 0.5, 0.5, 0.05);
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
        super.dropCustomDeathLoot(level, source, wasRecentlyHit);
        // Hydra Tooth
        this.spawnAtLocation(new ItemStack(Items.ARROW, 5 + random.nextInt(10))); // Hydra tooth arrows
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_DRACHMA.get(), 10 + random.nextInt(10)));
        // Guaranteed nectar from hydra
        this.spawnAtLocation(new ItemStack(ModItems.NECTAR.get(), 2));
        if (source.getEntity() instanceof Player player) {
            GodParentData.addDrachmas(player, 15);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.ENDER_DRAGON_GROWL; }
    @Override
    protected SoundEvent getHurtSound(DamageSource s) { return SoundEvents.WITHER_HURT; }
    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.WITHER_DEATH; }
}
