package com.demigodsfate.godparent;

import com.demigodsfate.DemigodsFate;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;

/**
 * Applies passive effects based on the player's god-parent.
 * Runs every tick to maintain buff status.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class PassiveHandler {

    private static final ResourceLocation ARES_DAMAGE_ID =
            ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "ares_damage_boost");
    private static final ResourceLocation HERMES_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "hermes_speed_boost");
    private static final ResourceLocation BELLONA_DAMAGE_ID =
            ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "bellona_damage_boost");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        GodParent god = GodParentData.getGodParent(player);
        if (god == null) return;

        // Apply passive effects every 2 seconds (40 ticks) to avoid spam
        if (player.tickCount % 40 != 0) return;

        switch (god) {
            case POSEIDON, NEPTUNE -> applySeaPassives(player);
            case ZEUS, JUPITER -> applySkyPassives(player);
            case ATHENA, MINERVA -> applyWisdomPassives(player);
            case ARES, MARS -> applyWarPassives(player);
            case HEPHAESTUS -> applyForgePassives(player);
            case APOLLO -> applySunPassives(player);
            case HERMES -> applyTravelPassives(player);
            case PLUTO -> applyDeathPassives(player);
            case BELLONA -> applyBellonaPassives(player);
        }
    }

    private static void applySeaPassives(Player player) {
        // Water breathing (permanent)
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, false));

        // Fast swimming via Dolphins Grace
        if (player.isInWater()) {
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100, 1, true, false));
        }

        // Rain heals
        if (player.level().isRaining() && player.level().canSeeSky(player.blockPosition())) {
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(0.5f);
            }
        }
    }

    private static void applySkyPassives(Player player) {
        // No fall damage handled in LivingFallEvent
        // Speed boost in rain
        if (player.level().isRaining() && player.level().canSeeSky(player.blockPosition())) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, true, false));
        }
    }

    private static void applyWisdomPassives(Player player) {
        // Night vision to see mob health bars (simulated with night vision)
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, false));
    }

    private static void applyWarPassives(Player player) {
        // +30% melee damage via attribute modifier
        var attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null && !attackAttr.hasModifier(ARES_DAMAGE_ID)) {
            attackAttr.addTransientModifier(new AttributeModifier(
                    ARES_DAMAGE_ID, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    private static void applyForgePassives(Player player) {
        // Fire immunity
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false));
    }

    private static void applySunPassives(Player player) {
        // Regeneration in sunlight
        long dayTime = player.level().getDayTime() % 24000;
        boolean isDaytime = dayTime < 12000;
        if (isDaytime && player.level().canSeeSky(player.blockPosition())) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, true, false));
        }

        // See in the dark
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, false));
    }

    private static void applyTravelPassives(Player player) {
        // +20% movement speed via attribute modifier
        var speedAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null && !speedAttr.hasModifier(HERMES_SPEED_ID)) {
            speedAttr.addTransientModifier(new AttributeModifier(
                    HERMES_SPEED_ID, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }

        // Better luck
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 100, 1, true, false));
    }

    private static void applyDeathPassives(Player player) {
        // Night vision (see ores through walls simulated)
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, false));
    }

    private static void applyBellonaPassives(Player player) {
        // +20% sword damage
        var attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null && !attackAttr.hasModifier(BELLONA_DAMAGE_ID)) {
            attackAttr.addTransientModifier(new AttributeModifier(
                    BELLONA_DAMAGE_ID, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    // Zeus/Jupiter: No fall damage
    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            GodParent god = GodParentData.getGodParent(player);
            if (god == GodParent.ZEUS || god == GodParent.JUPITER) {
                event.setCanceled(true);
            }
        }
    }

    // Fire damage immunity for Hephaestus children (backup for fire resistance)
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            GodParent god = GodParentData.getGodParent(player);
            if (god == GodParent.HEPHAESTUS) {
                if (event.getSource().is(DamageTypes.IN_FIRE)
                        || event.getSource().is(DamageTypes.ON_FIRE)
                        || event.getSource().is(DamageTypes.LAVA)) {
                    event.setNewDamage(0);
                }
            }
        }
    }
}
