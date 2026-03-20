package com.demigodsfate.godparent;

import net.minecraft.ChatFormatting;

/**
 * All available god parents in the mod.
 * Greek gods are available at Camp Half-Blood, Roman gods at Camp Jupiter.
 */
public enum GodParent {
    // Greek gods
    POSEIDON("Poseidon", Camp.GREEK, Domain.SEA, ChatFormatting.AQUA),
    ZEUS("Zeus", Camp.GREEK, Domain.SKY, ChatFormatting.YELLOW),
    ATHENA("Athena", Camp.GREEK, Domain.WISDOM, ChatFormatting.GRAY),
    ARES("Ares", Camp.GREEK, Domain.WAR, ChatFormatting.DARK_RED),
    HEPHAESTUS("Hephaestus", Camp.GREEK, Domain.FORGE, ChatFormatting.GOLD),
    APOLLO("Apollo", Camp.GREEK, Domain.SUN, ChatFormatting.YELLOW),
    HERMES("Hermes", Camp.GREEK, Domain.TRAVEL, ChatFormatting.GREEN),

    // Roman gods
    JUPITER("Jupiter", Camp.ROMAN, Domain.SKY, ChatFormatting.YELLOW),
    NEPTUNE("Neptune", Camp.ROMAN, Domain.SEA, ChatFormatting.DARK_AQUA),
    MARS("Mars", Camp.ROMAN, Domain.WAR, ChatFormatting.DARK_RED),
    PLUTO("Pluto", Camp.ROMAN, Domain.DEATH, ChatFormatting.DARK_GRAY),
    MINERVA("Minerva", Camp.ROMAN, Domain.WISDOM, ChatFormatting.GRAY),
    BELLONA("Bellona", Camp.ROMAN, Domain.WAR, ChatFormatting.RED);

    private final String displayName;
    private final Camp camp;
    private final Domain domain;
    private final ChatFormatting color;

    GodParent(String displayName, Camp camp, Domain domain, ChatFormatting color) {
        this.displayName = displayName;
        this.camp = camp;
        this.domain = domain;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public Camp getCamp() { return camp; }
    public Domain getDomain() { return domain; }
    public ChatFormatting getColor() { return color; }

    /**
     * Get the Greek counterpart of a Roman god, or vice versa.
     */
    public GodParent getCounterpart() {
        return switch (this) {
            case POSEIDON -> NEPTUNE;
            case ZEUS -> JUPITER;
            case ATHENA -> MINERVA;
            case ARES -> MARS;
            case NEPTUNE -> POSEIDON;
            case JUPITER -> ZEUS;
            case MINERVA -> ATHENA;
            case MARS -> ARES;
            default -> null; // No direct counterpart for Hephaestus, Apollo, Hermes, Pluto, Bellona
        };
    }

    public enum Camp {
        GREEK("Camp Half-Blood"),
        ROMAN("Camp Jupiter");

        private final String displayName;
        Camp(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum Domain {
        SEA, SKY, WISDOM, WAR, FORGE, SUN, TRAVEL, DEATH
    }
}
