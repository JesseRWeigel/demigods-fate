# Demigod's Fate — A Percy Jackson Minecraft Mod

A NeoForge mod for Minecraft 1.21.1 inspired by Rick Riordan's Percy Jackson universe. Choose your godly parent, wield celestial bronze weapons, fight mythological monsters, and embark on prophecy-driven quests through Camp Half-Blood, Camp Jupiter, the Underworld, Mount Olympus, and the Labyrinth.

## Download & Install (Windows / Mac / Linux)

### Requirements
- **Minecraft Java Edition** 1.21.1 (NOT Bedrock Edition)
- **Java 21** — download from https://adoptium.net/ if you don't have it
- **NeoForge** mod loader for 1.21.1

### Step-by-Step Install (Windows)

1. **Install Java 21** (if needed)
   - Go to https://adoptium.net/
   - Download **Temurin JDK 21** for Windows x64
   - Run the installer, check "Add to PATH"

2. **Install NeoForge**
   - Go to https://neoforged.net/
   - Select Minecraft version **1.21.1**
   - Click **Installer** and download the `.jar` file
   - Double-click the downloaded file (or right-click → Open With → Java)
   - Select **Install Client**, click **OK**
   - Wait for it to finish — this adds a NeoForge profile to your Minecraft Launcher

3. **Download the Mod**
   - Go to the [Releases page](https://github.com/JesseRWeigel/demigods-fate/releases/latest)
   - Download **`demigodsfate-0.1.0.jar`**

4. **Install the Mod**
   - Press **Win + R**, type `%appdata%\.minecraft\mods`, press Enter
   - If the `mods` folder doesn't exist, create it
   - Drag `demigodsfate-0.1.0.jar` into the `mods` folder

5. **Play!**
   - Open the **Minecraft Launcher**
   - Click the profile dropdown (bottom-left) and select **NeoForge 1.21.1**
   - Click **Play**

### Install (Mac)

Same as Windows, except the mods folder is at:
```
~/Library/Application Support/minecraft/mods/
```

### Install (Linux)

Same as Windows, except the mods folder is at:
```
~/.minecraft/mods/
```

## Getting Started — Your First 5 Minutes

Once you're in a world with the mod:

1. **Get claimed by a god** — Type `/demigod claim poseidon` (or any god you like)
2. **Check your abilities** — Type `/demigod abilities` to see your 3 powers
3. **Try your powers** — Press **R**, **V**, or **G** to use abilities
4. **Build your camp** — Type `/build camp_halfblood` to generate Camp Half-Blood around you
5. **Start a quest** — Type `/quest start the_lightning_thief` to begin the main storyline
6. **Explore the Labyrinth** — Type `/travel labyrinth` to enter a randomly generated maze dungeon
7. **Hunt monsters for drachmas** — Type `/bounty list` to pick up repeatable side quests

## Features

### 13 God Parents (7 Greek + 6 Roman)

| Greek | Roman | Domain |
|-------|-------|--------|
| Poseidon | Neptune | Sea — water breathing, water blast, earthquake, hurricane form |
| Zeus | Jupiter | Sky — no fall damage, lightning strike, wind gust, storm call |
| Athena | Minerva | Wisdom — see mob health, battle plan, aegis aura, tactical strike |
| Ares | Mars | War — +30% damage, bloodrage, war cry, weapon mastery |
| Hephaestus | — | Forge — fire immunity, forge fire, mechanical trap, bronze automaton |
| Apollo | — | Sun — arrow damage up, healing hymn, sun arrow, plague shot |
| Hermes | — | Travel — +20% speed, shadow step, steal, swiftness |
| — | Pluto | Death — see ores, raise dead, shadow meld, death grasp |
| — | Bellona | War — sword damage up, fury combo, rally, siege breaker |

### 39 Cooldown Abilities
Every god gives you 3 unique abilities bound to **R**, **V**, and **G** keys. The HUD in the top-left shows your god, drachma count, and ability cooldown status.

### 8 Mythological Monsters

| Monster | Difficulty | Special Mechanic |
|---------|-----------|-----------------|
| **Minotaur** | Boss | Charge attack — rushes in a straight line for devastating damage |
| **Medusa** | Boss | Stone Gaze — looking at her petrifies you! Use a **shield** to reflect the gaze back |
| **Hydra** | Boss | Head regrowth — non-fire damage makes it heal! Must use fire to kill |
| **Chimera** | Boss | Fire breath cone + poison tail |
| **Hellhound** | Medium | Shadow teleport — appears behind you from smoke |
| **Fury** | Hard | Fire whip — relentless 48-block pursuit range |
| **Empousai** | Medium | Charmspeak stun on first hit, fire teleport escape |
| **Cyclops** | Hard | Boulder throw from range, 14 damage melee |

### 8 Divine Weapons

| Weapon | God Affinity | Special |
|--------|-------------|---------|
| **Riptide** | Poseidon | Unbreakable, +50% damage near water |
| **Ivlivs** | Jupiter | 25% chance to summon lightning on hit |
| **Aegis Shield** | Zeus/Athena | Slows nearby mobs, fear pushback when blocking |
| **Backbiter** | Hermes | Damages everything, steal gold on kill |
| **Stygian Iron Sword** | Pluto | Life drain on hit, prevents monster respawn |
| **Leo's Forge Hammer** | Hephaestus | Sets mobs on fire, fire ring on hit |
| **Katoptris** | Aphrodite | Reveals nearby enemies through walls |
| **Morphing Spear** | Mars | Right-click cycles forms (spear/sword/axe) |

### 4 Weapon Tiers

| Tier | Strength | Special Property |
|------|----------|-----------------|
| **Celestial Bronze** | Above Iron | Passes through mortals (no damage to villagers/animals) |
| **Imperial Gold** | Diamond-level | Damages everything, higher damage but less durable |
| **Stygian Iron** | Above Diamond | Prevents monster respawn |
| **Bone Steel** | Netherite-level | Endgame (quest rewards only) |

### 3 Custom Dimensions

| Dimension | Command | Description |
|-----------|---------|-------------|
| **The Underworld** | `/travel underworld` | Dark caverns, rivers of fire, soul sand valleys |
| **Mount Olympus** | `/travel olympus` | Golden palaces floating above the clouds |
| **The Labyrinth** | `/travel labyrinth` | Procedurally generated maze — different every time! 6 room types including Daedalus's Workshop |

### 2 Camp Structures

| Camp | Command | Features |
|------|---------|---------|
| **Camp Half-Blood** | `/build camp_halfblood` | 12 themed cabins (Zeus=marble, Poseidon=prismarine, Hades=obsidian...), Big House with Chiron & Mr. D NPCs, amphitheater campfire, safe zone |
| **Camp Jupiter** | `/build camp_jupiter` | 5 cohort barracks, Principia HQ, Senate House, Temple Hill, Little Tiber river, safe zone |

### Quests & Side Content

- **The Lightning Thief** — 9-stage main questline with Oracle prophecy delivery
- **Monster Bounties** — 6 repeatable hunts (Hellhound Hunt, Fury Patrol, Cyclops Slayer, etc.)
- **Oracle Prophecies** — `/oracle prophecy` for random lore hints
- **Oracle Rebirth** — `/oracle rebirth <god>` to switch your god-parent (costs 50 drachmas)

### Items & Consumables

| Item | Effect |
|------|--------|
| **Ambrosia** | Heals 8 hearts (max 3/day or you burn!) |
| **Nectar** | Regeneration IV for 30 seconds |
| **Greek Fire** | Throwable green fire — burns through anything |
| **Golden Drachma** | Currency (dropped by monsters, used for Oracle services) |
| **Hermes Multivitamins** | Clears all negative effects |

## All Commands

| Command | Description |
|---------|-------------|
| `/demigod claim <god>` | Get claimed by a god-parent |
| `/demigod status` | Show your demigod status |
| `/demigod abilities` | List your abilities and cooldowns |
| `/demigod ability <1-3>` | Use an ability by number |
| `/demigod reset` | Reset your claiming |
| `/demigod drachma <amount>` | Add drachmas (admin) |
| `/oracle rebirth <god>` | Switch god-parent (50 drachmas) |
| `/oracle prophecy` | Receive a random prophecy |
| `/quest start the_lightning_thief` | Start the main quest |
| `/quest status` | Check quest progress |
| `/quest advance` | Advance quest stage (debug) |
| `/bounty list` | Show available monster bounties |
| `/bounty accept <number>` | Accept a bounty |
| `/bounty status` | Check bounty progress |
| `/build camp_halfblood` | Generate Camp Half-Blood |
| `/build camp_jupiter` | Generate Camp Jupiter |
| `/camp create <name> <radius>` | Create a custom safe zone |
| `/camp spawn chiron` | Spawn Chiron NPC |
| `/camp spawn mrd` | Spawn Mr. D NPC |
| `/travel underworld` | Enter the Underworld |
| `/travel olympus` | Ascend to Mount Olympus |
| `/travel labyrinth` | Enter the Labyrinth |
| `/travel overworld` | Return to the Overworld |

## Building from Source

Requires Java 21 (JDK).

```bash
git clone https://github.com/JesseRWeigel/demigods-fate.git
cd demigods-fate
./gradlew build
# JAR is at build/libs/demigodsfate-0.1.0.jar
```

## Design Document

Full game design spec in [`docs/design.md`](docs/design.md) — covers the Percy Jackson lore research, all 8 implementation phases, and future expansion plans.

## Contributing

Pull requests welcome! Areas that need help:
- Custom entity models (Blockbench) to replace vanilla placeholders
- Pixel art textures for items and blocks
- Additional questlines (Sea of Monsters, Heroes of Olympus, Trials of Apollo)
- Multiplayer testing and balancing

## Credits

Built for **Ambrose** by his dad Jesse, with help from Claude. Both camps are included because Ambrose wanted to experience the full Greek and Roman paths!

Based on Rick Riordan's Percy Jackson & the Olympians, Heroes of Olympus, and Trials of Apollo book series.

## License

MIT
