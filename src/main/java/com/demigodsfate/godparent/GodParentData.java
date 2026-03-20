package com.demigodsfate.godparent;

import com.demigodsfate.DemigodsFate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages god-parent data for players. Stores claiming status, chosen god,
 * drachma balance, and ability cooldowns.
 *
 * Uses player persistent data (NBT) to survive across sessions.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class GodParentData {
    private static final String TAG_KEY = "DemigodsFateData";
    private static final String TAG_GOD_PARENT = "GodParent";
    private static final String TAG_CLAIMED = "Claimed";
    private static final String TAG_DRACHMAS = "Drachmas";
    private static final String TAG_ABILITY_COOLDOWNS = "AbilityCooldowns";

    // Cache for quick access during gameplay
    private static final Map<UUID, CachedData> cache = new HashMap<>();

    public static boolean isClaimed(Player player) {
        return getCachedData(player).claimed;
    }

    @Nullable
    public static GodParent getGodParent(Player player) {
        return getCachedData(player).godParent;
    }

    public static void claimPlayer(Player player, GodParent godParent) {
        CachedData data = getCachedData(player);
        data.godParent = godParent;
        data.claimed = true;
        saveData(player, data);

        DemigodsFate.LOGGER.info("Player {} claimed by {}", player.getName().getString(), godParent.getDisplayName());
    }

    public static int getDrachmas(Player player) {
        return getCachedData(player).drachmas;
    }

    public static void addDrachmas(Player player, int amount) {
        CachedData data = getCachedData(player);
        data.drachmas = Math.max(0, data.drachmas + amount);
        saveData(player, data);
    }

    public static boolean spendDrachmas(Player player, int amount) {
        CachedData data = getCachedData(player);
        if (data.drachmas >= amount) {
            data.drachmas -= amount;
            saveData(player, data);
            return true;
        }
        return false;
    }

    public static long getAbilityCooldown(Player player, String abilityId) {
        CachedData data = getCachedData(player);
        return data.abilityCooldowns.getOrDefault(abilityId, 0L);
    }

    public static void setAbilityCooldown(Player player, String abilityId, long cooldownEndTick) {
        CachedData data = getCachedData(player);
        data.abilityCooldowns.put(abilityId, cooldownEndTick);
        saveData(player, data);
    }

    public static boolean isAbilityReady(Player player, String abilityId) {
        long cooldownEnd = getAbilityCooldown(player, abilityId);
        return player.level().getGameTime() >= cooldownEnd;
    }

    /**
     * Reset a player's claiming (Oracle rebirth).
     */
    public static void resetClaiming(Player player) {
        CachedData data = getCachedData(player);
        data.godParent = null;
        data.claimed = false;
        data.abilityCooldowns.clear();
        saveData(player, data);
    }

    // --- Persistence ---

    private static CachedData getCachedData(Player player) {
        return cache.computeIfAbsent(player.getUUID(), uuid -> loadData(player));
    }

    private static CachedData loadData(Player player) {
        CompoundTag persistent = player.getPersistentData();
        CachedData data = new CachedData();

        if (persistent.contains(TAG_KEY)) {
            CompoundTag tag = persistent.getCompound(TAG_KEY);
            data.claimed = tag.getBoolean(TAG_CLAIMED);
            if (tag.contains(TAG_GOD_PARENT)) {
                try {
                    data.godParent = GodParent.valueOf(tag.getString(TAG_GOD_PARENT));
                } catch (IllegalArgumentException e) {
                    data.godParent = null;
                }
            }
            data.drachmas = tag.getInt(TAG_DRACHMAS);
            if (tag.contains(TAG_ABILITY_COOLDOWNS)) {
                CompoundTag cooldowns = tag.getCompound(TAG_ABILITY_COOLDOWNS);
                for (String key : cooldowns.getAllKeys()) {
                    data.abilityCooldowns.put(key, cooldowns.getLong(key));
                }
            }
        }

        return data;
    }

    private static void saveData(Player player, CachedData data) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_CLAIMED, data.claimed);
        if (data.godParent != null) {
            tag.putString(TAG_GOD_PARENT, data.godParent.name());
        }
        tag.putInt(TAG_DRACHMAS, data.drachmas);

        CompoundTag cooldowns = new CompoundTag();
        data.abilityCooldowns.forEach(cooldowns::putLong);
        tag.put(TAG_ABILITY_COOLDOWNS, cooldowns);

        player.getPersistentData().put(TAG_KEY, tag);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Load data into cache when player joins
        cache.put(event.getEntity().getUUID(), loadData(event.getEntity()));
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        cache.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // Copy data when player respawns (death) or returns from End
        CompoundTag original = event.getOriginal().getPersistentData();
        if (original.contains(TAG_KEY)) {
            event.getEntity().getPersistentData().put(TAG_KEY, original.getCompound(TAG_KEY).copy());
            cache.put(event.getEntity().getUUID(), loadData(event.getEntity()));
        }
    }

    private static class CachedData {
        boolean claimed = false;
        @Nullable GodParent godParent = null;
        int drachmas = 0;
        Map<String, Long> abilityCooldowns = new HashMap<>();
    }
}
