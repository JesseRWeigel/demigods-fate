# Demigod's Fate — A Percy Jackson Minecraft Mod

A NeoForge mod for Minecraft 1.21.1 inspired by Rick Riordan's Percy Jackson universe. Choose your godly parent, wield celestial bronze weapons, and embark on prophecy-driven quests.

## Features (v0.1.0)

### God-Parent System
- **13 gods** — 7 Greek (Poseidon, Zeus, Athena, Ares, Hephaestus, Apollo, Hermes) + 6 Roman (Jupiter, Neptune, Mars, Pluto, Minerva, Bellona)
- **Claiming ceremony** via `/demigod claim <god>`
- **Passive abilities** per deity (water breathing, fire immunity, speed boost, etc.)
- **Oracle rebirth** to reset your parentage

### Cooldown Abilities
- **21 unique abilities** (3 per Greek god), bound to R / V / G keys
- Lightning Strike (Zeus), Water Blast (Poseidon), Healing Hymn (Apollo), Shadow Step (Hermes), and more
- Visual particles and sound effects for each ability
- HUD overlay showing cooldown status

### Weapons & Items
- **Celestial Bronze tier** — passes through mortals, stronger than iron
- **Imperial Gold tier** — diamond-level, damages everything
- **Riptide** — Percy's legendary unbreakable sword with Poseidon affinity bonus
- **Ambrosia & Nectar** — divine healing with overconsumption penalties
- **Greek Fire** — throwable green flames
- **Golden Drachmas** — currency dropped by monsters

### Quest System
- **Prophecy-driven** — the Oracle delivers cryptic 4-line prophecies
- **The Lightning Thief** questline with 9 stages
- Quest commands: `/quest start`, `/quest status`, `/quest advance`

## Commands

| Command | Description |
|---------|-------------|
| `/demigod claim <god>` | Get claimed by a god-parent |
| `/demigod reset` | Oracle rebirth (reset parentage) |
| `/demigod status` | Show your demigod status |
| `/demigod abilities` | List your abilities and cooldowns |
| `/demigod ability <1-3>` | Use an ability (also R/V/G keys) |
| `/demigod drachma <amount>` | Add drachmas (admin) |
| `/quest start <id>` | Start a quest |
| `/quest status` | Show active quest |
| `/quest advance` | Advance to next stage (debug) |

## Building

Requires Java 21 (JDK, not JRE).

```bash
./gradlew build
```

The mod JAR will be in `build/libs/`.

## Running (Development)

```bash
# Client (requires display)
./gradlew runClient

# Server (headless)
./gradlew runServer
```

## Design Document

Full design spec is in [`docs/design.md`](docs/design.md) — covers all 8 implementation phases including Camp Half-Blood, Camp Jupiter, monsters, dimensions, and future questlines.

## Built For

This mod was designed for **Ambrose** by his dad Jesse, with help from Claude. Both camps are included because Ambrose wanted to experience the full Greek and Roman paths!

## License

MIT
