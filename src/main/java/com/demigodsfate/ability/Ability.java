package com.demigodsfate.ability;

import com.demigodsfate.godparent.GodParent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base class for all demigod abilities.
 * Each ability has a cooldown, a god-parent requirement, and an execute method.
 */
public abstract class Ability {
    private final String id;
    private final String displayName;
    private final GodParent[] validParents;
    private final int cooldownTicks;
    private final int slot; // 0=R, 1=V, 2=G

    protected Ability(String id, String displayName, int cooldownTicks, int slot, GodParent... validParents) {
        this.id = id;
        this.displayName = displayName;
        this.cooldownTicks = cooldownTicks;
        this.slot = slot;
        this.validParents = validParents;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public int getCooldownTicks() { return cooldownTicks; }
    public int getSlot() { return slot; }

    public boolean canUse(Player player, GodParent godParent) {
        for (GodParent valid : validParents) {
            if (valid == godParent) return true;
        }
        return false;
    }

    /**
     * Execute the ability. Called server-side when the player presses the keybind.
     * @return true if the ability was used successfully (triggers cooldown)
     */
    public abstract boolean execute(Player player, Level level);

    /**
     * Get display component with ability color.
     */
    public Component getDisplayComponent() {
        return Component.literal(displayName);
    }
}
