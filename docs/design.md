# Demigod's Fate — Percy Jackson Minecraft Mod Design

> NeoForge mod for Minecraft 1.21.1, built for Ambrose
> Working title: "Demigod's Fate"

## Overview

A Percy Jackson-themed RPG mod where the player discovers one of two demigod camps in the open world, undergoes a claiming ceremony to be claimed by a god-parent, and embarks on prophecy-driven quests following the Percy Jackson storyline. Features cooldown-based abilities, divine weapons, passive deity buffs, and both Camp Half-Blood and Camp Jupiter as explorable locations.

### Key Design Decisions

- **Mod loader:** NeoForge (best API for custom entities, items, dimensions, GUIs)
- **Minecraft version:** 1.21.1 (stable ecosystem, mature tooling)
- **God selection:** Claiming ceremony after intro quest (book-accurate), with Oracle rebirth ritual + multiple character saves for replayability
- **Scope:** Both camps in v1 (Ambrose's request), geography-based discovery
- **Combat:** Cooldown abilities + findable divine weapons + passive buffs (Ambrose's hybrid design)
- **Quests:** Prophecy-driven (Oracle delivers cryptic prophecy, player figures out where to go)

---

## Section 1: World & Camps

Both camps generate as large custom structures in the Overworld, placed far apart (Camp Half-Blood in a forest/coastal biome, Camp Jupiter in a plains/hills biome). The player spawns normally in vanilla Minecraft and discovers a camp through exploration — or follows subtle signs (a satyr NPC wandering nearby, a golden drachma on the ground, monster attacks that escalate until you find safety).

### Camp Half-Blood

Located in a forest/coastal biome. Includes:
- **Half-Blood Hill** with Thalia's Pine and Peleus (dragon guardian)
- **The Big House** (Chiron NPC, quest hub)
- **20 themed cabins** (each visually distinct per the books):
  - Cabin 1 (Zeus): White marble, polished bronze doors, lightning bolt symbol
  - Cabin 2 (Hera): Graceful columns, honorary (no demigod children)
  - Cabin 3 (Poseidon): Rough gray stone, seashells and coral, ocean smell
  - Cabin 4 (Demeter): Grass roof, wildflowers, tomato vines
  - Cabin 5 (Ares): Ugly red paint, barbed wire, boar's head
  - Cabin 6 (Athena): Gray with owl carving, bookshelves inside
  - Cabin 7 (Apollo): Gold that glows in sunlight
  - Cabin 8 (Artemis): Silver that glows at night (honorary)
  - Cabin 9 (Hephaestus): Brick, smokestacks, metal roof
  - Cabin 10 (Aphrodite): Blue-and-white checkerboard, perfume
  - Cabin 11 (Hermes): Plain brown, caduceus symbol
  - Cabin 12 (Dionysus): Covered in grapevines
  - Cabin 13 (Hades): Solid obsidian, green fire torches, skull
  - Cabins 14-20: Iris (rainbow), Hypnos (poppies), Nemesis, Nike, Hebe, Tyche, Hecate (magical symbols)
- **Arena** (sword fighting training area)
- **Forge** (craft celestial bronze weapons)
- **Lava Climbing Wall** (two walls that clash together, pours lava)
- **Canoe Lake** (with naiads)
- **Archery Range**
- **Dining Pavilion** (magic goblets, food sacrifice mechanic)
- **Amphitheater** (campfire that changes color)
- **Pegasus Stables** (rideable pegasi)
- **The Woods** (monster-stocked forest, Labyrinth entrance at Zeus's Fist)
- **Strawberry Fields** (camp's cover story, harvestable)

### Camp Jupiter

Located in a plains/hills biome. Includes:
- **Caldecott Tunnel** entrance
- **Principia** (Praetor HQ, quest hub)
- **Senate House** (domed roof, voting mechanic for camp decisions)
- **Field of Mars** (war games arena, procedurally built fortress each time)
- **Five cohort barracks** (player joins one)
- **Temple Hill** (temples to each Roman god — Jupiter, Mars, Bellona, Mercury, Pluto, etc.)
- **New Rome** (shops, university, coliseum, fountains — a miniature city)
- **Little Tiber** river boundary (magical purification)
- **The Baths** (Roman bathhouse, healing station)

### Camp Mechanics

- Both camps have magical borders — hostile mobs can't enter, camps are safe zones
- NPCs populate both camps (Chiron, Mr. D, campers at CHB; Praetors, centurions, legionnaires at CJ)
- Camp activities available: Capture the Flag, war games, training exercises, crafting

---

## Section 2: Character System

### Claiming Ceremony

When the player first enters a camp, they start as **Unclaimed**. A short intro quest begins:
- **Camp Half-Blood path:** Fight the Minotaur attacking at the camp border (like Percy's arrival in Book 1). Survive and enter camp.
- **Camp Jupiter path:** Prove yourself to Lupa's wolf pack in a combat trial, then march to camp and join a cohort.

After the intro quest, the player visits the Oracle (CHB) or Temple Hill (CJ) and a personality/playstyle quiz subtly runs through dialogue choices. Then the claiming happens — a holographic symbol of the god appears above the player's head. The god selection is influenced by the player's choices but can also be overridden via a hidden "pray to a specific god" altar before the ceremony.

### Available God-Parents (v1)

#### Greek (Camp Half-Blood)

| God | Passives | Cooldown Abilities |
|-----|----------|-------------------|
| **Poseidon** | Water breathing, fast swimming, rain heals | Water Blast (ranged knockback), Earthquake (AoE ground slam), Hurricane Form (speed + damage aura) |
| **Zeus** | No fall damage, speed boost in rain, lightning resistance | Lightning Strike (targeted bolt), Wind Gust (pushback + flight burst), Storm Call (summons thunderstorm) |
| **Athena** | See mob health bars, permanent mini-map markers for structures, bonus XP | Battle Plan (reveals all nearby mobs through walls), Aegis Aura (temporary damage reduction for nearby allies), Tactical Strike (next hit guaranteed crit) |
| **Ares** | +30% melee damage, intimidation (weak mobs flee), bonus armor toughness | Bloodrage (damage + speed boost, costs health), War Cry (AoE fear), Weapon Mastery (temporary max attack speed) |
| **Hephaestus** | Fire immunity, auto-smelt ore when mining, faster crafting | Forge Fire (ranged fire attack), Mechanical Trap (place a turret/trap), Bronze Automaton (summon temporary combat ally) |
| **Apollo** | Increased arrow damage, regeneration in sunlight, see in the dark | Healing Hymn (heal self + nearby), Sun Arrow (explosive arrow), Plague Shot (poison AoE arrow) |
| **Hermes** | +20% movement speed, lockpicking (open any chest/door), better loot drops | Shadow Step (short-range teleport), Steal (pickpocket mobs for items), Swiftness (burst of extreme speed) |

#### Roman (Camp Jupiter)

> **Note:** Roman gods share identical passives with their Greek counterpart. Only the active abilities differ (Roman abilities have a more militaristic/disciplined flavor). Roman gods are implemented in Phase 6, after Greek gods ship in Phase 2.

| God | Passives | Cooldown Abilities |
|-----|----------|-------------------|
| **Jupiter** | Same as Zeus passives | Lightning Javelin (thrown bolt), Eagle's Flight (temporary flight), Imperium (command nearby mobs to fight for you) |
| **Neptune** | Same as Poseidon | Trident Strike, Tidal Wave, Kraken's Grasp |
| **Mars** | Same as Ares + bonus to group combat | Centurion's Charge, Shield Wall (damage immunity while blocking), Legion Strike (AoE) |
| **Pluto** | See ores through walls, shadow travel in darkness, undead ignore you | Raise Dead (summon skeleton warriors), Shadow Meld (invisibility in dark), Death Grasp (life drain) |
| **Minerva** | Same as Athena minus war focus, more crafting bonuses | Strategic Barrier (place walls), Knowledge Pulse (reveal secrets), Perfect Craft (bonus enchantment) |
| **Bellona** | +20% sword damage, shield bash knockback, armor doesn't slow you | War Goddess's Fury (multi-hit combo), Rally (buff allies), Siege Breaker (destroy blocks AoE) |

### Oracle Rebirth (Respec)
Visit the Oracle, pay 50 golden drachmas, complete a short trial quest. Resets your parentage and abilities. Quest progress carries over. Cooldown: once per in-game week.

### Multiple Character Saves
Use separate Minecraft worlds for different playthroughs (one Greek, one Roman, etc.). Each world has its own god-parent, quest progress, and inventory. This leverages Minecraft's native world save system rather than requiring custom save slot management.

---

## Section 3: Combat, Weapons & Items

### Magical Metals (Weapon Tiers)

| Metal | Tier | Properties | Where Found |
|-------|------|-----------|-------------|
| **Celestial Bronze** | Above Iron, below Diamond | Kills mod-added monsters and mythological entities. No damage to vanilla passive mobs (villagers, animals) — sword phases through them. Damages players in PvP. Greek standard. | Crafted at CHB forge with bronze ingots + moonlight ritual |
| **Imperial Gold** | Equal to Diamond | Kills monsters AND mortals. More damage but less durable. Roman standard. | Crafted at CJ forge with gold ingots + consecration at Temple of Jupiter |
| **Stygian Iron** | Above Diamond | Kills everything. Prevents monsters from respawning for longer. Rare. | Found only in Underworld locations or Hades/Pluto quests |
| **Bone Steel (Adamantine)** | Netherite equivalent | Endgame tier. Extremely rare. | Late-game quest rewards only |

### Divine Weapons (Findable Loot)

Unique named weapons scattered throughout the world in dungeons, quest rewards, and boss drops. Each has a **god affinity** — matching parent gives a bonus effect.

| Weapon | Type | Base Effect | God Affinity Bonus |
|--------|------|------------|-------------------|
| **Riptide** | Celestial Bronze Sword | Returns to inventory if lost, cannot break | Poseidon: +50% damage near water |
| **Ivlivs** | Imperial Gold Coin/Sword/Lance | Right-click toggles sword ↔ lance form | Jupiter: lightning damage on hit |
| **Katoptris** | Celestial Bronze Dagger | Shows nearby enemies through walls | Aphrodite/Venus: charmed mobs don't attack |
| **Aegis Shield** | Shield | Mobs within 5 blocks get Slowness (terror) | Zeus/Athena: AoE fear pushback on block |
| **Backbiter** | Hybrid Sword | Damages both monsters and players | Hermes: steal an item on kill |
| **Leo's Forge Hammer** | Hammer | Sets mobs on fire, auto-smelts mined blocks | Hephaestus: summons fire ring on hit |
| **Frank's Morphing Spear** | Spear | Right-click cycles weapon forms (spear, bow, sword) | Mars: each form gets +20% damage |

### Consumable Items

| Item | Effect |
|------|--------|
| **Ambrosia** | Heals 8 hearts instantly. Max 3/day — more deals fire damage. |
| **Nectar** | Regeneration IV for 30 seconds. Same overconsumption penalty. |
| **Golden Drachma** | Currency. Used for Iris-messages, Oracle services, camp shop. Dropped by monsters. |
| **Hermes Multivitamins** | Clears all negative effects, 10s potion immunity. |
| **Greek Fire** | Throwable. Green fire, burns through anything, water can't extinguish. Rare forge recipe. |

### Monster Drops

- Minotaur Horn → trophy + weapon crafting ingredient
- Hydra Tooth → poison-tipped arrows
- Nemean Lion Pelt → unbreakable chestplate (quest reward)
- Dracanae Scales → armor crafting material
- Hellhound Shadow Essence → shadow travel potions

---

## Section 4: Monsters & Enemies

Monsters spawn in the Overworld (especially near camps and quest locations) and in custom dimensions. They're tougher than vanilla mobs and drop drachmas + crafting materials. Key mechanic: **monsters killed with regular weapons respawn quickly, but Celestial Bronze/Imperial Gold kills slow their respawn. Stygian Iron prevents respawn entirely for that world session.**

### v1 Monster Roster

| Monster | Behavior | Difficulty | Where Found |
|---------|----------|-----------|-------------|
| **Minotaur** | Charges in straight lines, devastating melee | Mini-boss (intro quest boss) | Camp borders, Labyrinth |
| **Hellhounds** | Fast, shadow teleport, pack hunters | Medium | Forest, night spawns near camps |
| **Empousai** | Disguise as villagers, charmspeak stun, fire teleport | Medium | Towns, roads |
| **Dracanae** | Snake-women warriors, ranged javelin + melee | Medium | Groups near quest locations |
| **Furies (Kindly Ones)** | Fly, fire whips, pursue relentlessly | Hard | Triggered by quest events |
| **Cyclopes** | Huge, slow, massive damage, throw boulders | Hard | Caves, Sea of Monsters |
| **Laistrygonian Giants** | Throw flaming boulders from distance | Hard | Mountains, open fields |
| **Stymphalian Birds** | Razor feather ranged attack, flock behavior | Medium | Forests, lakesides |
| **Colchis Bulls** | Bronze automatons, fire breath, charge | Hard | Camp Jupiter war games, quests |
| **Hydra** | Multi-head, cut one = two more unless you use fire | Boss | Sea of Monsters quest |
| **Nemean Lion** | Invulnerable hide — must attack the mouth | Boss | Quest reward fight |
| **Medusa** | Stone gaze (don't look!), snake hair melee | Boss | Aunty Em's Garden (quest location) |
| **Manticore** | Flies, shoots poison thorns from tail | Boss | Quest encounters |
| **Chimera** | Fire breath, poison tail, lion/goat/snake | Boss | Gateway Arch quest |

### Boss Mechanics

Bosses have unique mechanics that reference the books:
- **Medusa:** Screen gets a "stone gaze" overlay when facing her — you must fight looking away or use a reflective shield
- **Nemean Lion:** All weapon damage blocked unless you hit the open mouth (timing-based)
- **Hydra:** Heads regrow unless stumps are cauterized with fire (torch, fire ability, Greek Fire)

---

## Section 5: Quest System & Storyline

### How Prophecies Work

1. Player visits the Oracle at Camp Half-Blood (the mummy in the attic of the Big House) or the Augur at Camp Jupiter (reads stuffed animal entrails)
2. Oracle delivers a cryptic 4-line prophecy in chat with dramatic visual effects (green mist particles, screen shake)
3. The prophecy hints at the destination, a key challenge, and a twist — just like the books
4. Player must interpret the prophecy and travel to the quest location
5. Quest objectives track in a custom HUD overlay (toggleable)
6. Between prophecies, free exploration / camp activities / monster hunting are always available

### Main Questline — The Lightning Thief (v1 Primary)

**Prophecy:**
> *You shall go west, and face the god who has turned,*
> *You shall find what was stolen, and see it safely returned,*
> *You shall be betrayed by one who calls you a friend,*
> *And you shall fail to save what matters most, in the end.*

**Quest Stages:**

| Stage | Location | Objective | Boss/Challenge |
|-------|----------|-----------|---------------|
| 1. Arrival | Camp border | Defeat the Minotaur, enter camp, get claimed | Minotaur (intro boss) |
| 2. Camp Training | Camp Half-Blood / Camp Jupiter | Complete 3 training exercises (combat, archery/forge, capture the flag) | Camp tutorial |
| 3. Receive Prophecy | Oracle / Augur | Receive the Lightning Thief prophecy | Dialogue + cutscene |
| 4. Aunty Em's Garden | Generated structure (plains) | Investigate the statue garden, defeat Medusa | Medusa (don't look!) |
| 5. The Gateway Arch | Generated structure (river biome) | Survive ambush by Echidna and the Chimera | Chimera (fire + poison) |
| 6. The Lotus Casino | Generated structure (desert) | Escape the time-trap casino (parkour + puzzle) | Time mechanic — stay too long and days pass outside |
| 7. The Underworld | Custom dimension (Nether reskin) | Enter DOA Recording Studios, cross the River Styx, confront Hades | Cerberus (puzzle — music soothes him), Hades dialogue |
| 8. The Showdown | Beach near camp | Fight Ares on the beach for the Master Bolt | Ares (god-tier boss fight) |
| 9. Mount Olympus | Custom structure above a mountain | Deliver the bolt to Zeus, receive reward | Throne room audience with Zeus NPC |

### Side Quests (Always Available)

| Quest | Type | Description |
|-------|------|-------------|
| **Capture the Flag** | Camp activity (post-v1 stretch) | Team-based PvE: player joins a cabin team, NPC campers on both sides, flag hidden in the Woods. Win by bringing enemy flag to your side. Monsters in the Woods add chaos. |
| **War Games** | Camp Jupiter activity (post-v1 stretch) | Assault a procedurally-built fortress on the Field of Mars. Player's cohort attacks, other cohorts defend. Siege weapons, water cannons, combat. |
| **Monster Bounties** | Repeatable | Chiron/Praetor posts bounties for specific monsters. Kill them for drachmas. |
| **Forge Commissions** | Crafting | NPCs request specific weapons/items. Craft and deliver for rewards. |
| **Iris Messages** | Lore | Toss a drachma into water + light for rainbow video calls with gods. Unlocks lore and hints. |
| **Labyrinth Runs** | Dungeon | Enter the Labyrinth at Zeus's Fist. Procedurally generated dungeon with randomized rooms, traps, and monsters. Rare loot. |
| **Pegasus Taming** | Exploration | Find wild pegasi in the world, tame them for fast travel |

### Future Questlines (Post-v1 Expansions)

- **Sea of Monsters** — Sail to the Bermuda Triangle, face Polyphemus, retrieve the Golden Fleece
- **The Titan's Curse** — Rescue mission with the Hunters of Artemis, fight Atlas
- **Battle of the Labyrinth** — Full Labyrinth dimension, find Daedalus
- **The Last Olympian** — Defend Manhattan from Kronos's army (large-scale battle event)
- **Heroes of Olympus** — Greek/Roman crossover, the Argo II, quest to Greece/Rome
- **Trials of Apollo** — Apollo as mortal companion NPC, restore the five Oracles

---

## Section 6: Dimensions & Key Locations

### Custom Dimensions

| Dimension | Theme | Access Method | Content |
|-----------|-------|--------------|---------|
| **The Underworld** | Dark, cavernous, rivers of fire/ice/sorrow | Enter through DOA Recording Studios (generated structure) | Charon's ferry, Fields of Asphodel (gray wasteland), Elysium (paradise biome), Fields of Punishment (fire/lava), Hades's Palace, River Styx. Used in Lightning Thief quest and Hades/Pluto storylines. |
| **Mount Olympus** | Floating city above the clouds, golden palaces | Elevator at the top of a tall mountain structure (Empire State Building equivalent) | Throne room of the gods, Olympian palaces, Annabeth's redesigned architecture. Quest destination and endgame hub. |
| **The Labyrinth** | Procedurally generated dungeon, ever-shifting | Enter at Zeus's Fist (rock formation in camp Woods) or other hidden entrances | Randomized rooms: trap rooms, monster arenas, treasure vaults, dead ends, Daedalus's Workshop. Repeatable dungeon content. Layout changes every entry. |
| **Tartarus** (future expansion) | Hellscape — poisonous air, rivers of fire, living terrain | Falls from the Underworld | Endgame dimension for Heroes of Olympus content. Constant damage unless you have ambrosia/nectar. Tartarus himself as a world boss. |

### Generated Overworld Structures

These spawn naturally in the world for exploration and quests:

| Structure | Biome | Purpose |
|-----------|-------|---------|
| **Aunty Em's Garden Emporium** | Plains/forest | Medusa's lair. Filled with stone statues (frozen villagers). Quest location. |
| **The Gateway Arch** | River biome | Chimera/Echidna encounter. Tall structure with observation deck. |
| **The Lotus Casino** | Desert | Time-trap puzzle dungeon. Flashy interior, arcade games, lotus flower food items. |
| **DOA Recording Studios** | Any biome (rare) | Entrance to the Underworld. Waiting room with ghost NPCs and Charon. |
| **Monster Lairs** | Various | Small dungeons with monster spawners and loot chests. Themed by monster type. |
| **Ancient Ruins** | Forest/mountain | Greek/Roman ruins with lore tablets, hidden caches, and monster guardians. |
| **Satyr Groves** | Forest | Friendly satyr NPCs who give hints about nearby camp locations and quests. |
| **Iris Fountain** | Any biome near water | Toss a drachma for an Iris-message. Communication hub. |

### Travel Between Camps

Since both camps are far apart in the Overworld:
- **Pegasus fast travel** — Tamed pegasi can fly between camps (loading screen with flight animation)
- **Shadow travel** — Pluto/Hades children can shadow-teleport between camps (costs health)
- **The Labyrinth** — Both camps have Labyrinth entrances that connect (dangerous shortcut)
- **Iris-message** — Can communicate with NPCs at the other camp remotely

---

## Section 7: Technical Architecture

### Project Structure

```
percy-jackson-mod/
├── docs/
│   └── design.md                    # This design document
├── src/main/java/com/demigodsfate/
│   ├── DemigodsFate.java            # Main mod class (@Mod entry point)
│   ├── config/                      # Mod configuration
│   ├── entity/                      # Custom entities
│   │   ├── monster/                 # Minotaur, Hydra, Medusa, etc.
│   │   ├── npc/                     # Chiron, Mr. D, campers, satyrs
│   │   └── companion/              # Pegasi, Mrs. O'Leary
│   ├── item/                        # Custom items
│   │   ├── weapon/                  # Celestial Bronze, Imperial Gold, divine weapons
│   │   ├── consumable/             # Ambrosia, Nectar, Greek Fire, drachmas
│   │   └── tool/                   # Forge Hammer, special tools
│   ├── block/                       # Custom blocks (forge, altar, oracle seat)
│   ├── effect/                      # Custom mob effects (Stone Gaze, Blessing of Ares, etc.)
│   ├── ability/                     # Cooldown ability system
│   │   ├── AbilityManager.java     # Tracks cooldowns, keybinds, unlocks
│   │   ├── AbilityKeyBinds.java    # Custom keybinds: R (ability 1), V (ability 2), G (ability 3)
│   │   └── abilities/              # One class per ability (WaterBlast, LightningStrike, etc.)
│   ├── godparent/                   # God-parent system
│   │   ├── GodParentManager.java   # Claiming, respec, save/load
│   │   ├── GodParent.java          # Enum/registry of all gods
│   │   └── PassiveHandler.java     # Applies passive buffs based on parentage
│   ├── quest/                       # Quest system
│   │   ├── QuestManager.java       # Track quest state, prophecies, objectives
│   │   ├── ProphecySystem.java     # Oracle interaction, prophecy delivery
│   │   └── quests/                 # Individual quest implementations
│   ├── world/                       # World generation
│   │   ├── structure/              # Camp Half-Blood, Camp Jupiter, Aunty Em's, etc.
│   │   ├── dimension/             # Underworld, Olympus, Labyrinth
│   │   └── biome/                 # Custom biomes for dimensions
│   ├── client/                      # Client-side only (renderers, GUI, HUD)
│   │   ├── renderer/              # Entity renderers, ability VFX
│   │   ├── screen/                # God selection GUI, quest tracker, Oracle screen
│   │   ├── hud/                   # Ability cooldown overlay, quest objective display
│   │   └── model/                 # Entity models (Blockbench exports)
│   ├── network/                     # Client-server packet sync
│   └── data/                        # Data generators (recipes, loot tables, tags)
├── src/main/resources/
│   ├── META-INF/neoforge.mods.toml # Mod metadata
│   ├── assets/demigodsfate/
│   │   ├── textures/               # Item, block, entity, GUI textures
│   │   ├── models/                 # Item and block models (JSON)
│   │   ├── lang/en_us.json         # English translations
│   │   ├── sounds/                 # Oracle voice, ability SFX, ambient camp sounds
│   │   └── shaders/               # Stone gaze effect, claiming ceremony glow
│   └── data/demigodsfate/
│       ├── recipes/                # Crafting recipes (Celestial Bronze, etc.)
│       ├── loot_tables/            # Monster drops, chest loot
│       ├── tags/                   # Block/item tags
│       └── worldgen/              # Biomes, dimensions, structures (JSON)
│           ├── dimension/
│           ├── dimension_type/
│           ├── structure/
│           └── template_pool/     # Jigsaw structure pieces
├── build.gradle                    # NeoForge build config
├── gradle.properties               # Version numbers, mod ID
└── settings.gradle                 # Project name
```

### Key Technical Decisions

| Decision | Choice | Reason |
|----------|--------|--------|
| **Ability system** | Custom (not Spell Engine dependency) | Full control over Percy Jackson-specific mechanics, no external dependency risk |
| **Quest system** | Custom (not FTB Quests) | Prophecy-driven design is too unique for generic quest frameworks. Reference FTB Quests for UI patterns. |
| **Structures** | Jigsaw + NBT templates | Build camps in-game with Structure Blocks, export as NBT. Jigsaw for modular/randomized pieces (Labyrinth). |
| **Dimensions** | JSON data-driven + custom chunk generators | Underworld/Olympus are static-ish dimensions. Labyrinth needs a custom procedural generator. |
| **Entity models** | Blockbench → Java model classes | Standard workflow. Custom models for all monsters and NPCs. |
| **Player data** | NeoForge Attachments API | Store god-parent, abilities, quest state, drachmas per-player. Persists across sessions. |
| **Networking** | NeoForge packet system | Sync ability cooldowns, quest state, claiming ceremony between server and client. |

### Build Order (Implementation Phases)

| Phase | Focus | Deliverable |
|-------|-------|-------------|
| **Phase 1** | Foundation | Mod scaffold, custom item tier (Celestial Bronze), basic weapon set, player data attachments for god-parent |
| **Phase 2** | Character system | Claiming ceremony flow, god-parent passives, ability system with cooldowns, HUD overlay |
| **Phase 3** | Camp Half-Blood | Structure generation, cabin builds (NBT), key NPCs (Chiron, Mr. D), camp safe zone |
| **Phase 4** | Monsters | 6-8 monster entities with models, AI, drops. Minotaur, Hellhounds, Empousai, Dracanae, Furies, Medusa |
| **Phase 5** | Quest system | Prophecy delivery, quest tracker HUD, Lightning Thief questline (all 9 stages) |
| **Phase 6** | Camp Jupiter | Second camp structure, Roman god variants, cohort system, war games |
| **Phase 7a** | Dimensions (static) | Underworld dimension, Mount Olympus |
| **Phase 7b** | Labyrinth | Procedurally generated dungeon dimension (complex — may extend to post-v1 with simplified version first) |
| **Phase 8** | Polish | Divine weapons, side quests, Oracle rebirth, sound effects, particle effects, balancing |
