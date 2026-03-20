#!/usr/bin/env node
/**
 * Creates proper 16x16 Minecraft-style pixel art textures.
 * Draws recognizable weapon/item shapes with correct color palettes.
 * Uses raw PNG creation (no dependencies).
 */

const fs = require('fs');
const path = require('path');

const ITEM_DIR = path.join(__dirname, '..', 'src/main/resources/assets/demigodsfate/textures/item');
const BLOCK_DIR = path.join(__dirname, '..', 'src/main/resources/assets/demigodsfate/textures/block');

// Simple PNG writer
function createPNG(width, height, pixels) {
    const crcTable = new Uint32Array(256);
    for (let n = 0; n < 256; n++) {
        let c = n;
        for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
        crcTable[n] = c;
    }
    function crc32(buf) {
        let c = 0xffffffff;
        for (let i = 0; i < buf.length; i++) c = crcTable[(c ^ buf[i]) & 0xff] ^ (c >>> 8);
        return (c ^ 0xffffffff) >>> 0;
    }
    function adler32(buf) {
        let a = 1, b = 0;
        for (let i = 0; i < buf.length; i++) { a = (a + buf[i]) % 65521; b = (b + a) % 65521; }
        return (b << 16) | a;
    }
    function makeChunk(type, data) {
        const len = Buffer.alloc(4); len.writeUInt32BE(data.length);
        const td = Buffer.concat([Buffer.from(type), data]);
        const crc = Buffer.alloc(4); crc.writeUInt32BE(crc32(td));
        return Buffer.concat([len, td, crc]);
    }

    // Build raw pixel data with filter bytes
    const rawData = [];
    for (let y = 0; y < height; y++) {
        rawData.push(0); // filter: none
        for (let x = 0; x < width; x++) {
            const idx = (y * width + x) * 4;
            rawData.push(pixels[idx], pixels[idx+1], pixels[idx+2], pixels[idx+3]);
        }
    }
    const raw = Buffer.from(rawData);

    // DEFLATE store
    const blocks = [];
    let offset = 0;
    while (offset < raw.length) {
        const remaining = raw.length - offset;
        const blockSize = Math.min(remaining, 65535);
        const isLast = offset + blockSize >= raw.length;
        blocks.push(isLast ? 1 : 0);
        blocks.push(blockSize & 0xff, (blockSize >> 8) & 0xff);
        blocks.push((~blockSize) & 0xff, ((~blockSize) >> 8) & 0xff);
        for (let i = 0; i < blockSize; i++) blocks.push(raw[offset + i]);
        offset += blockSize;
    }
    const adler = adler32(raw);
    const deflated = Buffer.concat([
        Buffer.from([0x78, 0x01]), Buffer.from(blocks),
        Buffer.from([(adler >> 24) & 0xff, (adler >> 16) & 0xff, (adler >> 8) & 0xff, adler & 0xff])
    ]);

    const ihdr = Buffer.alloc(13);
    ihdr.writeUInt32BE(width, 0); ihdr.writeUInt32BE(height, 4);
    ihdr[8] = 8; ihdr[9] = 6; // 8-bit RGBA

    return Buffer.concat([
        Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]),
        makeChunk('IHDR', ihdr),
        makeChunk('IDAT', deflated),
        makeChunk('IEND', Buffer.alloc(0))
    ]);
}

// Helper: create a 16x16 transparent canvas
function canvas() {
    return new Uint8Array(16 * 16 * 4); // All zeros = fully transparent
}

// Helper: set pixel
function px(data, x, y, r, g, b, a = 255) {
    if (x < 0 || x >= 16 || y < 0 || y >= 16) return;
    const idx = (y * 16 + x) * 4;
    data[idx] = r; data[idx+1] = g; data[idx+2] = b; data[idx+3] = a;
}

// Helper: fill rect
function rect(data, x1, y1, x2, y2, r, g, b, a = 255) {
    for (let y = y1; y <= y2; y++)
        for (let x = x1; x <= x2; x++)
            px(data, x, y, r, g, b, a);
}

// Helper: draw diagonal sword blade (bottom-left to top-right)
function swordBlade(data, r, g, b, highlight_r, highlight_g, highlight_b) {
    // Blade (diagonal)
    for (let i = 0; i < 10; i++) {
        px(data, 4+i, 11-i, r, g, b);
        px(data, 5+i, 11-i, highlight_r, highlight_g, highlight_b);
    }
    // Blade edge highlight
    for (let i = 0; i < 9; i++) {
        px(data, 5+i, 10-i, r, g, b);
    }
    // Guard
    px(data, 3, 12, 80, 80, 80);
    px(data, 4, 12, 100, 100, 100);
    px(d, 5, 12, 100, 100, 100);
    px(data, 6, 12, 80, 80, 80);
    // Handle
    px(data, 3, 13, 101, 67, 33);
    px(data, 2, 14, 101, 67, 33);
    px(data, 4, 13, 120, 80, 40);
    px(data, 3, 14, 120, 80, 40);
    // Pommel
    px(data, 1, 15, 80, 80, 80);
    px(data, 2, 15, 100, 100, 100);
}

// Helper: draw axe shape
function axeShape(data, r, g, b) {
    // Handle
    for (let i = 0; i < 10; i++) {
        px(data, 4+i, 11-i, 101, 67, 33);
    }
    // Axe head
    rect(data, 10, 1, 13, 5, r, g, b);
    px(data, 14, 2, r, g, b);
    px(data, 14, 3, r, g, b);
    px(data, 14, 4, r, g, b);
    // Highlight
    px(data, 11, 2, Math.min(r+40,255), Math.min(g+40,255), Math.min(b+40,255));
    px(data, 12, 3, Math.min(r+40,255), Math.min(g+40,255), Math.min(b+40,255));
}

// Helper: draw pickaxe
function pickaxeShape(data, r, g, b) {
    // Handle
    for (let i = 0; i < 10; i++) px(data, 4+i, 11-i, 101, 67, 33);
    // Pick head
    for (let i = 0; i < 5; i++) {
        px(data, 9+i, 2, r, g, b);
        px(data, 9+i, 3, r, g, b);
    }
    px(data, 8, 3, r, g, b);
    px(data, 14, 3, r, g, b);
    px(data, 10, 1, Math.min(r+30,255), Math.min(g+30,255), Math.min(b+30,255));
}

// Helper: ingot shape
function ingotShape(data, r, g, b) {
    // Ingot bar shape
    rect(data, 3, 6, 12, 10, r, g, b);
    rect(data, 4, 5, 11, 5, Math.min(r+30,255), Math.min(g+30,255), Math.min(b+30,255));
    rect(data, 4, 11, 11, 11, Math.max(r-30,0), Math.max(g-30,0), Math.max(b-30,0));
    // Top face highlight
    rect(data, 5, 5, 10, 6, Math.min(r+50,255), Math.min(g+50,255), Math.min(b+50,255));
}

// Helper: potion bottle
function bottleShape(data, r, g, b) {
    // Neck
    rect(data, 7, 2, 8, 4, 200, 200, 220);
    // Cork
    rect(data, 7, 1, 8, 1, 139, 90, 43);
    // Body
    rect(data, 5, 5, 10, 12, r, g, b);
    rect(data, 4, 6, 4, 11, r, g, b);
    rect(data, 11, 6, 11, 11, r, g, b);
    // Glass highlight
    px(data, 6, 6, Math.min(r+60,255), Math.min(g+60,255), Math.min(b+60,255));
    px(data, 6, 7, Math.min(r+40,255), Math.min(g+40,255), Math.min(b+40,255));
    // Bottom
    rect(data, 5, 13, 10, 13, Math.max(r-40,0), Math.max(g-40,0), Math.max(b-40,0));
}

// Helper: coin shape
function coinShape(data, r, g, b) {
    // Circular coin
    const cx = 7, cy = 7, radius = 5;
    for (let y = 0; y < 16; y++) {
        for (let x = 0; x < 16; x++) {
            const dist = Math.sqrt((x-cx)*(x-cx) + (y-cy)*(y-cy));
            if (dist <= radius) {
                if (dist <= radius - 1.5) {
                    px(data, x, y, r, g, b);
                } else {
                    px(data, x, y, Math.max(r-50,0), Math.max(g-50,0), Math.max(b-50,0));
                }
            }
        }
    }
    // Inner detail - owl eye
    px(data, 6, 6, Math.min(r+40,255), Math.min(g+40,255), Math.min(b+40,255));
    px(data, 8, 6, Math.min(r+40,255), Math.min(g+40,255), Math.min(b+40,255));
    px(data, 7, 8, Math.max(r-20,0), Math.max(g-20,0), Math.max(b-20,0));
}

// Helper: horn/fang shape
function hornShape(data, r, g, b) {
    px(data, 8, 3, r, g, b);
    px(data, 8, 4, r, g, b);
    px(data, 7, 5, r, g, b); px(data, 8, 5, r, g, b);
    px(data, 7, 6, r, g, b); px(data, 8, 6, r, g, b);
    px(data, 6, 7, r, g, b); px(data, 7, 7, r, g, b); px(data, 8, 7, r, g, b);
    px(data, 6, 8, r, g, b); px(data, 7, 8, r, g, b);
    px(data, 5, 9, r, g, b); px(data, 6, 9, r, g, b); px(data, 7, 9, r, g, b);
    px(data, 5, 10, r, g, b); px(data, 6, 10, r, g, b);
    px(data, 5, 11, Math.max(r-20,0), Math.max(g-20,0), Math.max(b-20,0));
    px(data, 6, 11, r, g, b);
    px(data, 5, 12, Math.max(r-30,0), Math.max(g-30,0), Math.max(b-30,0));
}

// Helper: shield shape
function shieldShape(data, r, g, b, emblem_r, emblem_g, emblem_b) {
    // Shield outline
    for (let y = 2; y <= 13; y++) {
        const halfWidth = y <= 7 ? 5 : 5 - Math.floor((y - 7) * 0.8);
        for (let x = 7 - halfWidth; x <= 8 + halfWidth; x++) {
            if (x === 7 - halfWidth || x === 8 + halfWidth || y === 2 || y === 13) {
                px(data, x, y, Math.max(r-40,0), Math.max(g-40,0), Math.max(b-40,0));
            } else {
                px(data, x, y, r, g, b);
            }
        }
    }
    // Emblem center
    px(data, 7, 6, emblem_r, emblem_g, emblem_b);
    px(data, 8, 6, emblem_r, emblem_g, emblem_b);
    px(data, 7, 7, emblem_r, emblem_g, emblem_b);
    px(data, 8, 7, emblem_r, emblem_g, emblem_b);
    px(data, 7, 8, emblem_r, emblem_g, emblem_b);
    px(data, 8, 8, emblem_r, emblem_g, emblem_b);
}

// Helper: shovel
function shovelShape(data, r, g, b) {
    // Handle
    for (let i = 0; i < 9; i++) px(data, 4+i, 11-i, 101, 67, 33);
    // Shovel head
    rect(data, 11, 1, 13, 4, r, g, b);
    px(data, 10, 2, r, g, b);
    px(data, 14, 2, r, g, b);
    px(data, 10, 3, r, g, b);
    px(data, 14, 3, r, g, b);
    px(data, 12, 1, Math.min(r+40,255), Math.min(g+40,255), Math.min(b+40,255));
}

// Helper: hoe
function hoeShape(data, r, g, b) {
    // Handle
    for (let i = 0; i < 9; i++) px(data, 4+i, 11-i, 101, 67, 33);
    // Hoe head
    rect(data, 11, 2, 14, 3, r, g, b);
    px(data, 12, 1, r, g, b);
    px(data, 13, 1, r, g, b);
}

// === GENERATE ALL TEXTURES ===
const CB = [205, 127, 50];     // Celestial Bronze
const CB_H = [230, 155, 80];   // CB highlight
const IG = [255, 215, 0];      // Imperial Gold
const IG_H = [255, 235, 80];   // IG highlight
const SI = [40, 30, 50];       // Stygian Iron
const SI_H = [70, 50, 90];     // SI highlight

const textures = {};

// Celestial Bronze tools
let d;
d = canvas(); swordBlade(d, ...CB, ...CB_H); textures['celestial_bronze_sword'] = d;
d = canvas(); axeShape(d, ...CB); textures['celestial_bronze_axe'] = d;
d = canvas(); pickaxeShape(d, ...CB); textures['celestial_bronze_pickaxe'] = d;
d = canvas(); shovelShape(d, ...CB); textures['celestial_bronze_shovel'] = d;
d = canvas(); hoeShape(d, ...CB); textures['celestial_bronze_hoe'] = d;
d = canvas(); ingotShape(d, ...CB); textures['celestial_bronze_ingot'] = d;

// Imperial Gold tools
d = canvas(); swordBlade(d, ...IG, ...IG_H); textures['imperial_gold_sword'] = d;
d = canvas(); axeShape(d, ...IG); textures['imperial_gold_axe'] = d;
d = canvas(); pickaxeShape(d, ...IG); textures['imperial_gold_pickaxe'] = d;
d = canvas(); shovelShape(d, ...IG); textures['imperial_gold_shovel'] = d;
d = canvas(); hoeShape(d, ...IG); textures['imperial_gold_hoe'] = d;
d = canvas(); ingotShape(d, ...IG); textures['imperial_gold_ingot'] = d;

// Stygian Iron
d = canvas(); swordBlade(d, ...SI, ...SI_H); textures['stygian_iron_sword'] = d;
d = canvas(); ingotShape(d, ...SI); textures['stygian_iron_ingot'] = d;

// Divine weapons - Riptide (glowing cyan sword)
d = canvas(); swordBlade(d, 0, 180, 220, 100, 220, 255);
// Add glow pixels around blade
for (let i = 0; i < 8; i++) { px(d, 6+i, 10-i, 100, 220, 255, 80); px(d, 4+i, 12-i, 0, 150, 200, 60); }
textures['riptide'] = d;

// Ivlivs (golden coin-sword)
d = canvas(); swordBlade(d, ...IG, ...IG_H);
coinShape(d, 200, 180, 0); // Overlay small coin at bottom
textures['ivlivs'] = d;

// Aegis Shield
d = canvas(); shieldShape(d, 180, 180, 200, 100, 200, 100);
textures['aegis_shield'] = d;

// Backbiter (half bronze / half steel)
d = canvas();
for (let i = 0; i < 5; i++) { px(d, 4+i, 11-i, ...CB); px(d, 5+i, 11-i, ...CB_H); }
for (let i = 5; i < 10; i++) { px(d, 4+i, 11-i, 160, 160, 170); px(d, 5+i, 11-i, 200, 200, 210); }
for (let i = 0; i < 9; i++) px(d, 5+i, 10-i, 180, 140, 100);
px(d, 3, 12, 80, 80, 80); px(d, 4, 12, 100, 100, 100); px(d, 5, 12, 100, 100, 100); px(d, 6, 12, 80, 80, 80);
px(d, 3, 13, 101, 67, 33); px(d, 2, 14, 101, 67, 33); px(d, 4, 13, 120, 80, 40); px(d, 3, 14, 120, 80, 40);
px(d, 1, 15, 80, 80, 80); px(d, 2, 15, 100, 100, 100);
textures['backbiter'] = d;

// Forge Hammer (orange-bronze hammer)
d = canvas();
// Handle
for (let i = 0; i < 8; i++) px(d, 4+i, 11-i, 101, 67, 33);
// Hammer head
rect(d, 9, 1, 14, 5, 220, 120, 30);
rect(d, 10, 2, 13, 4, 255, 150, 50);
px(d, 11, 3, 255, 200, 100); // Hot center
textures['forge_hammer'] = d;

// Katoptris (purple dagger)
d = canvas();
// Short blade
for (let i = 0; i < 6; i++) { px(d, 6+i, 9-i, 170, 100, 200); px(d, 7+i, 9-i, 200, 140, 230); }
px(d, 5, 10, 80, 80, 80); px(d, 6, 10, 100, 100, 100); px(d, 7, 10, 80, 80, 80);
px(d, 5, 11, 101, 67, 33); px(d, 4, 12, 101, 67, 33);
px(d, 6, 11, 120, 80, 40); px(d, 5, 12, 120, 80, 40);
textures['katoptris'] = d;

// Morphing Spear (red spear)
d = canvas();
// Long handle
for (let i = 0; i < 12; i++) px(d, 3+i, 12-i, 101, 67, 33);
// Spear tip
px(d, 13, 1, 180, 30, 30); px(d, 14, 0, 200, 40, 40); px(d, 14, 1, 220, 50, 50);
px(d, 15, 0, 230, 60, 60);
textures['morphing_spear'] = d;

// Consumables
d = canvas(); rect(d, 4, 5, 11, 11, 255, 200, 80); rect(d, 5, 6, 10, 10, 255, 220, 120);
px(d, 6, 7, 255, 240, 160); px(d, 7, 8, 255, 240, 160);
textures['ambrosia'] = d;

d = canvas(); bottleShape(d, 255, 165, 0); textures['nectar'] = d;
d = canvas(); coinShape(d, 255, 215, 0); textures['golden_drachma'] = d;
d = canvas(); bottleShape(d, 0, 200, 50); textures['greek_fire'] = d;
d = canvas(); bottleShape(d, 50, 180, 50); textures['hermes_multivitamin'] = d;

// Monster drops
d = canvas(); hornShape(d, 160, 100, 50); textures['minotaur_horn'] = d;
d = canvas(); hornShape(d, 40, 40, 50); textures['hellhound_fang'] = d;

d = canvas(); // Dracanae scale - green diamond
rect(d, 6, 4, 9, 11, 34, 139, 34);
px(d, 7, 3, 50, 160, 50); px(d, 8, 3, 50, 160, 50);
px(d, 5, 6, 34, 139, 34); px(d, 10, 6, 34, 139, 34);
px(d, 7, 7, 80, 200, 80); px(d, 8, 7, 80, 200, 80);
textures['dracanae_scale'] = d;

d = canvas(); // Cyclops eye - blue orb
for (let y = 0; y < 16; y++) for (let x = 0; x < 16; x++) {
    const dist = Math.sqrt((x-7.5)*(x-7.5) + (y-7.5)*(y-7.5));
    if (dist < 5) px(d, x, y, 100, 150, 255);
    if (dist < 3) px(d, x, y, 150, 200, 255);
    if (dist < 1.5) px(d, x, y, 30, 30, 60); // Pupil
}
textures['cyclops_eye'] = d;

d = canvas(); // Fury whip fragment - orange leather strip
rect(d, 6, 2, 8, 13, 200, 100, 30);
rect(d, 7, 1, 7, 14, 220, 120, 40);
px(d, 8, 3, 255, 150, 50); px(d, 8, 5, 255, 150, 50); // Fire sparks
px(d, 6, 7, 255, 140, 40);
textures['fury_whip_fragment'] = d;

// Save all textures
let count = 0;
for (const [name, pixels] of Object.entries(textures)) {
    const png = createPNG(16, 16, pixels);
    fs.writeFileSync(path.join(ITEM_DIR, `${name}.png`), png);
    count++;
}

// Block textures - simple tiled patterns
function blockTexture(r, g, b) {
    const d = canvas();
    for (let y = 0; y < 16; y++) {
        for (let x = 0; x < 16; x++) {
            // Brick-like pattern
            const isEdge = (y % 4 === 0) || ((x + (y < 8 ? 0 : 4)) % 8 === 0);
            if (isEdge) {
                px(d, x, y, Math.max(r-40,0), Math.max(g-40,0), Math.max(b-40,0));
            } else {
                const vary = ((x * 7 + y * 13) % 20) - 10;
                px(d, x, y, Math.max(0,Math.min(255,r+vary)), Math.max(0,Math.min(255,g+vary)), Math.max(0,Math.min(255,b+vary)));
            }
        }
    }
    return d;
}

const blocks = {
    'celestial_bronze_block': blockTexture(205, 127, 50),
    'imperial_gold_block': blockTexture(255, 215, 0),
    'oracle_seat': blockTexture(100, 50, 130),
};

for (const [name, pixels] of Object.entries(blocks)) {
    fs.writeFileSync(path.join(BLOCK_DIR, `${name}.png`), createPNG(16, 16, pixels));
    count++;
}

console.log(`Generated ${count} pixel art textures`);
