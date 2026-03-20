#!/usr/bin/env python3
"""Generate Percy Jackson mod music using Meta's MusicGen AI model."""

import subprocess
import sys
import os

# Check and install audiocraft if needed
try:
    import audiocraft
except ImportError:
    print("audiocraft not found, installing...")
    subprocess.check_call([sys.executable, "-m", "pip", "install", "audiocraft"])
    print("audiocraft installed!")

try:
    import soundfile as sf
except ImportError:
    print("soundfile not found, installing...")
    subprocess.check_call([sys.executable, "-m", "pip", "install", "soundfile"])

import torch
import soundfile as sf
import numpy as np

OUTPUT_DIR = os.path.join(
    os.path.dirname(__file__),
    "..", "src", "main", "resources", "assets", "demigodsfate", "sounds"
)

TRACKS = [
    {
        "name": "camp_halfblood_ambient",
        "prompt": "peaceful Greek camp ambient music, gentle acoustic guitar, soft flute melody, warm summer day feeling, birds chirping in background, campfire crackle, relaxed folk style, pastoral, 80bpm, loopable",
        "duration": 30,
    },
    {
        "name": "camp_jupiter_ambient",
        "prompt": "Roman military camp ambient music, distant marching drums, brass horns in background, disciplined and orderly atmosphere, stoic orchestral pads, legionnaire march feel, structured rhythm, 100bpm, loopable",
        "duration": 30,
    },
    {
        "name": "quest_battle",
        "prompt": "epic Greek mythology battle music, fast tempo 150bpm, dramatic orchestral, fighting monsters, intense war drums, aggressive strings, brass fanfare, heroic action combat theme, minor key, cinematic",
        "duration": 20,
    },
    {
        "name": "underworld_ambient",
        "prompt": "dark eerie underworld ambient music, deep echoing cavern, slow deep drums, haunting ghostly choir, river of souls flowing, ominous low drone, reverb heavy, Greek Hades realm, 60bpm, loopable",
        "duration": 30,
    },
    {
        "name": "olympus_theme",
        "prompt": "majestic heavenly Mount Olympus theme, golden harps, triumphant brass fanfare, divine celestial choir, major key, glorious orchestral, clouds and sunlight feeling, godlike grandeur, 90bpm",
        "duration": 20,
    },
    {
        "name": "labyrinth_ambient",
        "prompt": "tense mysterious labyrinth ambient music, dripping water echoes, stone corridor reverb, suspenseful strings, feeling lost and confused, occasional metallic clang, minor key, unsettling, 70bpm, loopable",
        "duration": 30,
    },
    {
        "name": "claiming_ceremony",
        "prompt": "dramatic reveal moment music, building orchestral crescendo from quiet to powerful, then massive triumphant chord, divine blessing bestowed, magical shimmer, heavenly choir burst, epic climax",
        "duration": 10,
    },
    {
        "name": "victory_fanfare",
        "prompt": "quest complete victory fanfare, bright triumphant brass horns, joyful celebration, major key, heroic achievement, short uplifting orchestral flourish, RPG victory jingle",
        "duration": 8,
    },
]


def generate_tracks():
    from audiocraft.models import MusicGen

    print("Loading MusicGen medium model...")
    model = MusicGen.get_pretrained('facebook/musicgen-medium')
    print(f"Model loaded! Sample rate: {model.sample_rate}")
    print(f"CUDA available: {torch.cuda.is_available()}")
    if torch.cuda.is_available():
        print(f"GPU: {torch.cuda.get_device_name(0)}")

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    for i, track in enumerate(TRACKS, 1):
        print(f"\n[{i}/{len(TRACKS)}] Generating {track['name']} ({track['duration']}s)...")
        print(f"  Prompt: {track['prompt'][:80]}...")

        model.set_generation_params(
            duration=track["duration"],
            top_k=250,
            top_p=0.0,
            temperature=1.0,
        )

        wav = model.generate([track["prompt"]])

        # wav shape: [batch, channels, samples]
        audio = wav[0]  # First (only) batch item

        # Normalize to prevent clipping
        peak = audio.abs().max()
        if peak > 0:
            audio = audio / peak * 0.9

        path = os.path.join(OUTPUT_DIR, f"{track['name']}.wav")

        # Remove old file if exists
        if os.path.exists(path):
            os.remove(path)

        audio_np = audio.cpu().numpy().squeeze()
        sf.write(path, audio_np, model.sample_rate)
        size_kb = os.path.getsize(path) / 1024
        print(f"  Saved: {path} ({size_kb:.0f}KB)")

    print("\n" + "=" * 60)
    print("All 8 Percy Jackson mod tracks generated!")
    print(f"Output directory: {os.path.abspath(OUTPUT_DIR)}")
    print("=" * 60)


if __name__ == "__main__":
    generate_tracks()
