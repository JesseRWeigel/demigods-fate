package com.demigodsfate.item.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * Celestial Bronze sword — kills monsters but passes through mortals.
 * "Mortal" entities (villagers, passive animals) take no damage.
 * Players still take damage (PvP works).
 */
public class CelestialBronzeSwordItem extends SwordItem {
    public CelestialBronzeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Celestial bronze passes through mortal entities (villagers, animals)
        // but damages monsters, players, and mod entities
        if (isMortal(target)) {
            return false; // No damage, no durability loss
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    /**
     * Check if an entity is "mortal" (cannot be harmed by celestial bronze).
     * Mortals = villagers + passive animals.
     * Players and hostile mobs are NOT mortal for gameplay purposes.
     */
    protected boolean isMortal(Entity entity) {
        if (entity instanceof Player) return false; // PvP works
        if (entity instanceof AbstractVillager) return true;
        if (entity instanceof Animal) return true;
        return false;
    }
}
