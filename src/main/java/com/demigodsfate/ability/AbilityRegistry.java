package com.demigodsfate.ability;

import com.demigodsfate.ability.abilities.*;
import com.demigodsfate.godparent.GodParent;

import java.util.*;

/**
 * Registry of all abilities mapped to their god-parent.
 * Each god has 3 abilities mapped to slots 0 (R), 1 (V), 2 (G).
 */
public class AbilityRegistry {
    private static final Map<GodParent, List<Ability>> ABILITIES = new HashMap<>();

    static {
        // Poseidon
        register(GodParent.POSEIDON, new WaterBlastAbility());
        register(GodParent.POSEIDON, new EarthquakeAbility());
        register(GodParent.POSEIDON, new HurricaneFormAbility());

        // Zeus
        register(GodParent.ZEUS, new LightningStrikeAbility());
        register(GodParent.ZEUS, new WindGustAbility());
        register(GodParent.ZEUS, new StormCallAbility());

        // Athena
        register(GodParent.ATHENA, new BattlePlanAbility());
        register(GodParent.ATHENA, new AegisAuraAbility());
        register(GodParent.ATHENA, new TacticalStrikeAbility());

        // Ares
        register(GodParent.ARES, new BloodrageAbility());
        register(GodParent.ARES, new WarCryAbility());
        register(GodParent.ARES, new WeaponMasteryAbility());

        // Hephaestus
        register(GodParent.HEPHAESTUS, new ForgeFireAbility());
        register(GodParent.HEPHAESTUS, new MechanicalTrapAbility());
        register(GodParent.HEPHAESTUS, new BronzeAutomatonAbility());

        // Apollo
        register(GodParent.APOLLO, new HealingHymnAbility());
        register(GodParent.APOLLO, new SunArrowAbility());
        register(GodParent.APOLLO, new PlagueShotAbility());

        // Hermes
        register(GodParent.HERMES, new ShadowStepAbility());
        register(GodParent.HERMES, new StealAbility());
        register(GodParent.HERMES, new SwiftnessAbility());
    }

    private static void register(GodParent god, Ability ability) {
        ABILITIES.computeIfAbsent(god, k -> new ArrayList<>()).add(ability);
    }

    /**
     * Get all abilities for a god-parent.
     */
    public static List<Ability> getAbilities(GodParent god) {
        return ABILITIES.getOrDefault(god, Collections.emptyList());
    }

    /**
     * Get ability for a god-parent at a specific slot.
     */
    public static Ability getAbility(GodParent god, int slot) {
        List<Ability> abilities = getAbilities(god);
        if (slot >= 0 && slot < abilities.size()) {
            return abilities.get(slot);
        }
        return null;
    }
}
